package com.jangi10.mineblacksmith.core.data;

/**
 * [ForgingStats]
 * 5대 스탯 + 무게를 담는 그릇 (수정본: 더하기 기능 추가)
 */
public class ForgingStats {
    // 5대 스탯
    private final double hardness;     // 강도
    private final double durability;   // 내구성
    private final double elasticity;   // 탄성
    private final double polishing;    // 연마
    private final double conductivity; // 전도성

    // 물리 속성
    private final double weight;       // 무게

    public ForgingStats(double hardness, double durability, double elasticity,
                        double polishing, double conductivity, double weight) {
        this.hardness = hardness;
        this.durability = durability;
        this.elasticity = elasticity;
        this.polishing = polishing;
        this.conductivity = conductivity;
        this.weight = weight;
    }

    // [에러 해결] 스탯끼리 더하는 기능 추가
    // 조립할 때(검날+손잡이) 스탯을 합치기 위해 필수입니다.
    public ForgingStats add(ForgingStats other) {
        return new ForgingStats(
                this.hardness + other.hardness,
                this.durability + other.durability,
                this.elasticity + other.elasticity,
                this.polishing + other.polishing,
                this.conductivity + other.conductivity,
                this.weight + other.weight
        );
    }

    // Getters
    public double getHardness() { return hardness; }
    public double getDurability() { return durability; }
    public double getElasticity() { return elasticity; }
    public double getPolishing() { return polishing; }
    public double getConductivity() { return conductivity; }
    public double getWeight() { return weight; }

    public static ForgingStats empty() {
        return new ForgingStats(0, 0, 0, 0, 0, 0);
    }
}