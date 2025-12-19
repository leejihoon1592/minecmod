package com.jangi10.mineblacksmith.core.logic;

/**
 * [온도 시스템 핵심 로직]
 *
 * 1) 색상 판정: T_max 기준 퍼센트 구간 :contentReference[oaicite:4]{index=4}
 * - Yellow: 40~60
 * - Orange: 60~70
 * - Red   : 70~90
 * - White : 90~100
 *
 * 2) 단조 허용 구간: T_base 기준 난이도 비율 적용 :contentReference[oaicite:5]{index=5}
 */
public class TemperatureLogic {

    // ---- 색상 퍼센트 구간(기획서) ----
    public static final double YELLOW_MIN = 0.40;
    public static final double YELLOW_MAX = 0.60;

    public static final double ORANGE_MIN = 0.60;
    public static final double ORANGE_MAX = 0.70;

    public static final double RED_MIN = 0.70;
    public static final double RED_MAX = 0.90;

    public static final double WHITE_MIN = 0.90;
    public static final double WHITE_MAX = 1.00;

    /**
     * 현재 온도를 색상으로 판정한다.
     * @param currentTemp 현재 온도
     * @param tMax 해당 주괴의 최고온도(녹는점 직전) :contentReference[oaicite:6]{index=6}
     */
    public static TemperatureColor getColor(double currentTemp, double tMax) {
        if (tMax <= 0) return TemperatureColor.COLD;
        double p = currentTemp / tMax; // tempPercent :contentReference[oaicite:7]{index=7}

        if (p < YELLOW_MIN) return TemperatureColor.COLD;
        if (p < YELLOW_MAX) return TemperatureColor.YELLOW;
        if (p < ORANGE_MAX) return TemperatureColor.ORANGE;
        if (p < RED_MAX) return TemperatureColor.RED;
        if (p <= WHITE_MAX) return TemperatureColor.WHITE;

        // 100% 초과 = 기획서상 "추가 가열 시 소멸" 영역 :contentReference[oaicite:8]{index=8}
        return TemperatureColor.WHITE;
    }

    /**
     * 제작 난이도 기반 단조 허용 구간(T_base 기준)을 계산한다.
     * 예: T_base=2610, 70~90% => 1827~2349 :contentReference[oaicite:9]{index=9}
     */
    public static TemperatureWindowResult getForgeWindow(double currentTemp, double tBase, int difficulty) {
        DifficultyTemperatureProfile p = DifficultyTemperatureProfile.of(difficulty);
        double min = tBase * p.minRatio();
        double max = tBase * p.maxRatio();
        boolean in = (currentTemp >= min && currentTemp <= max);
        return new TemperatureWindowResult(min, max, in);
    }

    /**
     * 난이도 구간의 "온도 편차"를 하강속도로 나눈 값(기획서 표의 유지 시간 계산식) :contentReference[oaicite:10]{index=10}
     * - 유지 시간(초) = (T_base*(maxRatio-minRatio)) / coolingPerSecond
     */
    public static double getNaturalHoldTimeSeconds(double tBase, int difficulty) {
        DifficultyTemperatureProfile p = DifficultyTemperatureProfile.of(difficulty);
        double deviation = tBase * (p.maxRatio() - p.minRatio());
        return deviation / p.coolingPerSecond();
    }

    /**
     * 난이도 하강속도를 "틱당 냉각량"으로 변환.
     * Minecraft: 20 ticks/sec
     */
    public static double getCoolingPerTick(double tBaseIgnored, int difficulty) {
        DifficultyTemperatureProfile p = DifficultyTemperatureProfile.of(difficulty);
        return p.coolingPerSecond() / 20.0;
    }

    private TemperatureLogic() {}
}
