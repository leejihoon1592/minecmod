package com.jangi10.mineblacksmith.core.logic;

/**
 * [원거리 전투 결과]
 * - finalDamage: 최종 피해
 * - chargeSpeedMultiplier: 차징 속도 배율(시위/활대 영향)
 * - projectilePowerMultiplier: 발사력 배율(시위/활대 영향)
 * - critical: 치명타 여부
 */
public class RangedCombatResult {

    private final double finalDamage;
    private final double chargeSpeedMultiplier;
    private final double projectilePowerMultiplier;
    private final boolean critical;

    public RangedCombatResult(double finalDamage,
                              double chargeSpeedMultiplier,
                              double projectilePowerMultiplier,
                              boolean critical) {
        this.finalDamage = finalDamage;
        this.chargeSpeedMultiplier = chargeSpeedMultiplier;
        this.projectilePowerMultiplier = projectilePowerMultiplier;
        this.critical = critical;
    }

    public double getFinalDamage() { return finalDamage; }
    public double getChargeSpeedMultiplier() { return chargeSpeedMultiplier; }
    public double getProjectilePowerMultiplier() { return projectilePowerMultiplier; }
    public boolean isCritical() { return critical; }
}
