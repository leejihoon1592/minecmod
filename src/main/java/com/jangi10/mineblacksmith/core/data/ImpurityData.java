package com.jangi10.mineblacksmith.core.data;

/**
 * 광석에 포함된 불순물(Impurity) 데이터입니다.
 * 정제 과정에서 특정 온도에 도달해야 제거됩니다.
 */
public class ImpurityData {
    private final String name;           // 불순물 이름 (예: "유황", "돌가루")
    private final double removalTempMin; // 제거 시작 온도 (최소)
    private final double removalTempMax; // 제거 허용 온도 (최대)
    // 추후 확장: 불순물 제거 실패 시 페널티 등을 추가할 수 있음

    public ImpurityData(String name, double removalTempMin, double removalTempMax) {
        this.name = name;
        this.removalTempMin = removalTempMin;
        this.removalTempMax = removalTempMax;
    }

    public String getName() { return name; }
    public double getRemovalTempMin() { return removalTempMin; }
    public double getRemovalTempMax() { return removalTempMax; }

    /**
     * 현재 온도가 이 불순물을 제거하기 적합한지 확인합니다.
     */
    public boolean isRemovableAt(double currentTemp) {
        return currentTemp >= removalTempMin && currentTemp <= removalTempMax;
    }
}