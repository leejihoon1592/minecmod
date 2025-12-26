package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.*;
import com.jangi10.mineblacksmith.core.registry.FuelRegistry;
import com.jangi10.mineblacksmith.core.registry.MaterialRegistry;
import com.jangi10.mineblacksmith.core.registry.OreRegistry;

import java.util.Random;

/**
 * [FurnaceLogic]
 * - 1틱 업데이트(온도 계산 + 불순물 정제 진행)
 * - 주괴 추출(틀 우클릭) 처리
 */
public class FurnaceLogic {

    private static final Random RNG = new Random();

    /**
     * 노 1틱 처리:
     * 1) 연료 기반 온도 업데이트
     * 2) 광물/불순물 정제 진행(현재 목표 1개만)
     * 3) (추가) 온도×난이도 허용 구간 밖이면 정제 진행 리셋 + 실패 누적
     */
    public static void tick(FurnaceSession session) {

        if (session == null) return;
        if (session.getFuelId() == null) return;

        // 연료 로드 (너 프로젝트 FuelRegistry는 getFuelData 사용)
        FuelData fuel = FuelRegistry.get(session.getFuelId()).orElse(null);
        if (fuel == null) return;

        // 1) 온도 업데이트
        double nextTemp = HeatPhysics.calculateNextTemperature(
                session.getTemperature(),
                fuel,
                session.isBellowsPulse()
        );
        session.setTemperature(nextTemp);

        // 풀무 펄스는 1틱 소비
        session.setBellowsPulse(false);

        // ==========================================
        // 난이도 기반 "자연 냉각" 적용
        // - 난이도가 설정되어 있을 때만 적용
        // - 초당 냉각속도를 tick(20분의 1초) 단위로 환산해서 온도를 조금 깎는다
        // ==========================================
        if (session.getForgingDifficulty() > 0) {
            double coolPerTick = TemperatureLogic.getCoolingPerTick(session.getBaseHeatTemp(), session.getForgingDifficulty());
            double cooled = session.getTemperature() - coolPerTick;

            // 상온 아래로는 내려가지 않게(기본 20도)
            if (cooled < 20.0) cooled = 20.0;

            session.setTemperature(cooled);
        }


        // 2) 광물 없으면 정제 진행 없음
        if (session.getOreId() == null) return;

        OreData ore = OreRegistry.get(session.getOreId());

        if (ore == null) return;

        // 불순물 다 끝났으면 종료
        if (session.getImpurityIndex() >= ore.getImpurities().size()) return;

        // ==========================================
        // 온도 색상 기반 "가공 가능" 체크
        // - tMax가 아직 없으므로, 현재 단계에서는 baseHeatTemp를 임시로 tMax처럼 사용
        // - 나중에 실제 IngotData의 maxTemperature를 연결하면 됨
        // ==========================================
        double tMaxTemp = 1.0;

// ore -> resultIngotId -> IngotData(meltingPoint) 로 tMax 계산
        String ingotId = ore.getResultIngotId();
        IngotData ingot = MaterialRegistry.getMaterial(ingotId).orElse(null);

        if (ingot != null && ingot.getMeltingPoint() > 0) {
            tMaxTemp = ingot.getMeltingPoint();
        } else if (session.getBaseHeatTemp() > 0) {
            // fallback(임시값): 아직 데이터가 없을 때만 사용
            tMaxTemp = session.getBaseHeatTemp();
        }

        TemperatureColor color = TemperatureLogic.getColor(session.getTemperature(), tMaxTemp);

        if (!ForgeAvailability.canForgeByColor(color)) {
            if (session.getRefiningProgress() > 0) {
                session.setRefiningProgress(0);
            }
            session.addForgeWindowFailCount(1);
            return;
        }


        // ==========================================
        // 온도 × 제작 난이도 허용 구간 체크
        // - 허용 구간 밖이면: 정제 진행 리셋 + 실패 누적 후 종료
        // ==========================================
        if (session.getBaseHeatTemp() > 0 && session.getForgingDifficulty() > 0) {
            TemperatureWindowResult w = TemperatureLogic.getForgeWindow(
                    session.getTemperature(),
                    session.getBaseHeatTemp(),
                    session.getForgingDifficulty()
            );

            if (!w.isInRange()) {
                // 진행도 리셋
                if (session.getRefiningProgress() > 0) {
                    session.setRefiningProgress(0);
                }

                // 실패 누적
                session.addForgeWindowFailCount(1);

                // ------------------------------------------
                // 실패 누적이 일정 횟수(예: 5회)마다 주괴틀 더러움 증가
                // (기획서: 틀 관리/세척 중요도 연결)
                // ------------------------------------------
                if (session.getForgeWindowFailCount() % 5 == 0) {
                    session.getMoldState().addDirt(1);
                }

                return;
            }

        }

        // 3) 현재 목표 불순물 1개 처리
        OreData.Impurity target = ore.getImpurities().get(session.getImpurityIndex());

        RefiningResult result = RefiningLogic.processTick(
                session.getTemperature(),
                target.minTemp,
                target.maxTemp,
                session.getRefiningProgress()
        );


        // ==========================================
        // 온도 관리 성공 시 실패 누적 회복(서서히 감소)
        // - 정제가 실제로 진행 중일 때만(진행도 증가) 회복
        // ==========================================
        if (result.getProgress() > session.getRefiningProgress()) {
            if (session.getForgeWindowFailCount() > 0) {
                session.setForgeWindowFailCount(session.getForgeWindowFailCount() - 1);
            }
        }


        session.setRefiningProgress(result.getProgress());

        // 4) 완료 시 다음 불순물로 넘어감
        if (result.isFinished()) {
            session.getExtractedImpurities().add(target.id);
            session.setImpurityIndex(session.getImpurityIndex() + 1);
            session.setRefiningProgress(0);
        }
    }

    /**
     * 주괴틀 우클릭 "추출"
     * - 불순물 남아있으면: 성공은 가능하되 품질 페널티
     * - 온도×난이도 실패 누적도 페널티로 반영
     */
    public static IngotExtractionResult extractIngot(FurnaceSession session) {
        if (session == null) {
            return new IngotExtractionResult(false, null, 0, 0, 0, 0, "세션 없음");
        }
        if (session.getOreId() == null) {
            return new IngotExtractionResult(false, null, 0, 0, 0,
                    session.getMoldState().getDirtLevel(), "광물이 없음");
        }

        OreData ore = OreRegistry.get(session.getOreId());
        if (ore == null) {
            return new IngotExtractionResult(false, null, 0, 0, 0,
                    session.getMoldState().getDirtLevel(), "등록되지 않은 광물");
        }

        int totalImp = ore.getImpurities().size();
        int removed = session.getExtractedImpurities().size();
        int remaining = Math.max(0, totalImp - removed);

        // ==========================================
        // 추출 시점 온도 색상 체크(빨강이 아니면 추가 페널티)
        // ==========================================
        double tMaxTemp = 1.0;

        String ingotId = ore.getResultIngotId();
        IngotData ingot = MaterialRegistry.getMaterial(ingotId).orElse(null);

        if (ingot != null && ingot.getMeltingPoint() > 0) {
            tMaxTemp = ingot.getMeltingPoint();
        } else if (session.getBaseHeatTemp() > 0) {
            tMaxTemp = session.getBaseHeatTemp();
        }

        TemperatureColor color = TemperatureLogic.getColor(session.getTemperature(), tMaxTemp);
        boolean goodTemp = (color == TemperatureColor.RED);


        // 페널티 계산(불순물 남은 개수 기반)
        int statPenalty = 0;
        int weightBonus = 0;
        for (int i = 0; i < remaining; i++) {
            statPenalty += randRange(1, 3);
            weightBonus += randRange(1, 3);
        }
        // 온도가 빨강이 아니면(노/주/하양/차가움) 추가 품질 저하
        if (!goodTemp) {
            statPenalty += randRange(1, 3);
            weightBonus += randRange(1, 2);
        }


        // 온도×난이도 실패 누적 페널티: 3회당 1스택 추가
        int fails = session.getForgeWindowFailCount();
        int extraPenaltyStacks = fails / 3;
        for (int i = 0; i < extraPenaltyStacks; i++) {
            statPenalty += randRange(1, 3);
            weightBonus += randRange(1, 3);
        }

        // 틀 더러움 누적
        int dirtAdd = 1;
        if (remaining > 0) dirtAdd += 1;
        if (session.getMoldState().isDirty()) dirtAdd += 1;
        session.getMoldState().addDirt(dirtAdd);
        if (!goodTemp) dirtAdd += 1;

        // 추출 후 세션 초기화
        String resultIngotId = ore.getResultIngotId();
        session.setOreId(null);
        session.setImpurityIndex(0);
        session.setRefiningProgress(0);
        session.getExtractedImpurities().clear();
        session.resetForgeWindowFailCount();

        String msg;
        if (remaining == 0 && goodTemp) {
            msg = "순수 주괴 추출";
        } else if (remaining == 0) {
            msg = "온도 불량 상태 추출 → 품질 저하";
        } else {
            msg = "불순물 " + remaining + "개 남음 → 품질 저하";
            if (!goodTemp) msg += " + 온도 불량";
        }

        return new IngotExtractionResult(
                true,
                resultIngotId,
                remaining,
                statPenalty,
                weightBonus,
                session.getMoldState().getDirtLevel(),
                msg
        );
    }

    private static int randRange(int min, int max) {
        return min + RNG.nextInt((max - min) + 1);
    }

    private FurnaceLogic() {}
}
