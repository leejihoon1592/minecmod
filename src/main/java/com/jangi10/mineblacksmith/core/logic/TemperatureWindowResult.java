package com.jangi10.mineblacksmith.core.logic;

/**
 * [온도 허용 구간 결과]
 * - T_base 기준으로 난이도 허용 min/max 계산 값
 * - 현재 온도가 구간 안인지 판정
 */
public class TemperatureWindowResult {

    private final double minTemp;
    private final double maxTemp;
    private final boolean inRange;

    public TemperatureWindowResult(double minTemp, double maxTemp, boolean inRange) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.inRange = inRange;
    }

    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public boolean isInRange() { return inRange; }
}
