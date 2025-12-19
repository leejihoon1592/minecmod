package com.jangi10.mineblacksmith.core.data;

/**
 * [뇌: 프로그래머 메모]
 * 모든 주괴(Ingot) 및 합금의 메타 데이터를 정의하는 클래스.
 * 껍데기(Class)가 닫히기 전에 생성자랑 메서드가 다 들어가야 해.
 */
public class IngotData {
    private final String id;
    private final String name;
    private final ForgingStats baseStats;

    // 온도 관련 물리 속성
    private final double meltingPoint;
    private final double boilingPoint;
    private final int difficultyLevel;

    // 시각적 속성
    private final int baseColor;

    // [메인 생성자]
    public IngotData(String id, String name, ForgingStats baseStats,
                     double meltingPoint, double boilingPoint,
                     int difficultyLevel, int baseColor) {
        this.id = id;
        this.name = name;
        this.baseStats = baseStats;
        this.meltingPoint = meltingPoint;
        this.boilingPoint = boilingPoint;
        this.difficultyLevel = difficultyLevel;
        this.baseColor = baseColor;
    }

    // [Getter 메서드들]
    public String getId() { return id; }
    public String getName() { return name; }
    public ForgingStats getBaseStats() { return baseStats; }
    public double getMeltingPoint() { return meltingPoint; }
    public double getBoilingPoint() { return boilingPoint; }
    public int getDifficultyLevel() { return difficultyLevel; }
    public int getBaseColor() { return baseColor; }

    /**
     * 현재 온도가 가공 가능한 범위인지 확인
     */
    public boolean isWorkableTemperature(double currentTemp) {
        return currentTemp >= meltingPoint && currentTemp <= boilingPoint;
    }

    // [JSON Parsing용 기본 생성자]
    // 아까 이 녀석이 괄호 밖으로 튀어 나가서 에러 났던 거야. 안으로 넣었어.
    public IngotData() {
        this.id = "";
        this.name = "";
        this.baseStats = null;
        this.meltingPoint = 0;
        this.boilingPoint = 0;
        this.difficultyLevel = 0;
        this.baseColor = 0;
    }

} // <--- 클래스 끝은 무조건 여기여야 해.