package com.jangi10.mineblacksmith.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * [FurnaceSession]
 * - 블록/타일이 들고 있을 "노 내부 상태"를 코어 자료구조로 분리
 * - 연료/광물 투입, 온도, 불순물 진행도, 제거 단계 등을 보관한다.
 *
 * 기획서: 연료+풀무 순간가열, 온도게이지, 불순물 제거 단계 :contentReference[oaicite:7]{index=7}
 */
public class FurnaceSession {

    private int burnTicks;
    private int burnTicksTotal;

    // 허용 구간 이탈(실패) 누적 카운트: 패널티/품질저하 연결용
    private int forgeWindowFailCount;

    // ==============================
    // 온도 × 제작 난이도(단조) 세션값
    // ==============================

    // 기준 가열 온도(T_base): 아이템/주괴마다 계산된 기준 가열 온도 :contentReference[oaicite:11]{index=11}
    private double baseHeatTemp;


    // 제작 난이도(1~): 난이도별 허용 구간이 달라진다. :contentReference[oaicite:12]{index=12}
    private int forgingDifficulty;


    private String oreId;               // 투입 광물 ID
    private String fuelId;              // 사용 연료 ID
    private double temperature;         // 현재 온도(°C)

    // 불순물 제거 진행
    private int impurityIndex;          // 현재 목표 불순물 인덱스(0~2)
    private int refiningProgress;       // 0~200(10초) 누적 진행도 (RefiningLogic.REQUIRED_TICKS)
    private final List<String> extractedImpurities = new ArrayList<>();

    // 풀무(순간 가열) 플래그: 타일이 클릭 이벤트에서 1틱만 true로 넘기면 됨
    private boolean bellowsPulse;

    // 주괴틀 상태
    private final IngotMoldState moldState;

    public FurnaceSession(IngotMoldState moldState) {
        this.moldState = (moldState == null) ? new IngotMoldState() : moldState;
        this.temperature = 20.0; // 상온 시작
    }

    public String getOreId() { return oreId; }
    public void setOreId(String oreId) { this.oreId = oreId; }

    public String getFuelId() { return fuelId; }
    public void setFuelId(String fuelId) { this.fuelId = fuelId; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public int getImpurityIndex() { return impurityIndex; }
    public void setImpurityIndex(int impurityIndex) { this.impurityIndex = Math.max(0, impurityIndex); }

    public int getRefiningProgress() { return refiningProgress; }
    public void setRefiningProgress(int refiningProgress) { this.refiningProgress = Math.max(0, refiningProgress); }

    public List<String> getExtractedImpurities() { return extractedImpurities; }

    public boolean isBellowsPulse() { return bellowsPulse; }
    public void setBellowsPulse(boolean bellowsPulse) { this.bellowsPulse = bellowsPulse; }

    public IngotMoldState getMoldState() { return moldState; }

    public double getBaseHeatTemp() { return baseHeatTemp; }
    public void setBaseHeatTemp(double baseHeatTemp) { this.baseHeatTemp = baseHeatTemp; }

    public int getForgingDifficulty() { return forgingDifficulty; }
    public void setForgingDifficulty(int forgingDifficulty) { this.forgingDifficulty = forgingDifficulty; }

    public int getForgeWindowFailCount() { return forgeWindowFailCount; }
    public void setForgeWindowFailCount(int forgeWindowFailCount) { this.forgeWindowFailCount = Math.max(0, forgeWindowFailCount); }

    public void addForgeWindowFailCount(int amount) { this.forgeWindowFailCount = Math.max(0, this.forgeWindowFailCount + Math.max(0, amount)); }
    public void resetForgeWindowFailCount() { this.forgeWindowFailCount = 0; }

    public int getBurnTicks() { return burnTicks; }
    public void setBurnTicks(int v) { this.burnTicks = Math.max(0, v); }

    public int getBurnTicksTotal() { return burnTicksTotal; }
    public void setBurnTicksTotal(int v) { this.burnTicksTotal = Math.max(0, v); }



}
