package com.jangi10.mineblacksmith.core.logic;

public enum WeaponDamageType {
    SLASH(0.6, 0.4),   // 참격: 연마 0.6 + 강도 0.4
    BLUNT(0.4, 0.6),   // 타격: 연마 0.4 + 강도 0.6
    PIERCE(0.5, 0.5),  // 관통: 연마 0.5 + 강도 0.5
    RANGED(0.0, 0.0);  // 원거리는 별도 공식(탄성/강도)

    private final double polishCoef;
    private final double strengthCoef;

    WeaponDamageType(double polishCoef, double strengthCoef) {
        this.polishCoef = polishCoef;
        this.strengthCoef = strengthCoef;
    }

    public double polishCoef() { return polishCoef; }
    public double strengthCoef() { return strengthCoef; }
}
