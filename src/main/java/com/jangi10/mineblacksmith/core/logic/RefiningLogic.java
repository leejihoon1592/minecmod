package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ImpurityData;

public class RefiningLogic {

    // 기획서: 10초 유지 필요 -> Minecraft 20 ticks/sec * 10 = 200 ticks
    public static final int REQUIRED_TICKS = 200;

    /**
     * 정제 진행도를 갱신합니다. (매 틱 호출)
     * @param currentTemp 현재 노 온도
     * @param targetImpurity 현재 제거 목표인 불순물
     * @param currentProgress 현재 누적된 진행도
     * @return 갱신된 결과 (RefiningResult)
     */
    public static RefiningResult processTick(double currentTemp, ImpurityData targetImpurity, int currentProgress) {
        // 1. 불순물이 없거나 이미 끝난 경우
        if (targetImpurity == null) {
            return new RefiningResult(currentProgress, RefiningStatus.COMPLETE, true);
        }

        // 2. 온도 판정
        RefiningStatus status;
        if (currentTemp < targetImpurity.getRemovalTempMin()) {
            status = RefiningStatus.TOO_COLD;
        } else if (currentTemp > targetImpurity.getRemovalTempMax()) {
            status = RefiningStatus.TOO_HOT;
        } else {
            status = RefiningStatus.IN_RANGE;
        }

        // 3. 진행도 계산
        int newProgress = currentProgress;

        if (status == RefiningStatus.IN_RANGE) {
            newProgress++; // 적정 온도면 진행도 상승
        } else {
            // 범위 밖이면 진행도 감소 (난이도에 따라 감소폭 조절 가능, 일단 1씩 감소)
            if (newProgress > 0) newProgress--;
        }

        // 4. 완료 체크
        boolean isFinished = (newProgress >= REQUIRED_TICKS);

        return new RefiningResult(newProgress, status, isFinished);
    }
}