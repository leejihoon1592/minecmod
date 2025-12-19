package com.jangi10.mineblacksmith.core.data;

public class FuelData {
    // ✅ 레지스트리 키(우리 내부 ID) - 예: "coal", "charcoal"
    private String id;

    // 표시/연결 정보
    private String name;           // 연료 이름 (표시용)
    private String targetItemId;   // 연결될 아이템 ID (예: "minecraft:coal")

    private double averageTempMin;
    private double averageTempMax;
    private double maxTempLimit;

    // JSON용 기본 생성자
    public FuelData() {}

    public FuelData(String id, String name, String targetItemId, double min, double max, double limit) {
        this.id = id;
        this.name = name;
        this.targetItemId = targetItemId;
        this.averageTempMin = min;
        this.averageTempMax = max;
        this.maxTempLimit = limit;
    }

    // ✅ FuelRegistry가 찾는 메서드
    public String getId() { return id; }


    public String getName() { return name; }
    public String getTargetItemId() { return targetItemId; }

    public double getBaseTargetTemp() {
        return (averageTempMin + averageTempMax) / 2.0;
    }

    public double getMaxTempLimit() { return maxTempLimit; }
}
