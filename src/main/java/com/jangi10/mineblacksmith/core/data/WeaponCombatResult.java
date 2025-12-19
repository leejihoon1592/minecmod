package com.jangi10.mineblacksmith.core.data;

/**
 * 무기/전투 계산 결과 표준 컨테이너
 * API/아이템/엔티티 공용 반환 타입
 */
public class WeaponCombatResult {

    private final double finalDamage;
    private final double armorReductionRatio;
    private final int defenseStability;
    private final int protection;
    private final double speedMultiplier;
    private final boolean critical;

    public WeaponCombatResult(double finalDamage,
                              double armorReductionRatio,
                              int defenseStability,
                              int protection,
                              double speedMultiplier,
                              boolean critical) {
        this.finalDamage = finalDamage;
        this.armorReductionRatio = armorReductionRatio;
        this.defenseStability = defenseStability;
        this.protection = protection;
        this.speedMultiplier = speedMultiplier;
        this.critical = critical;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public double getArmorReductionRatio() {
        return armorReductionRatio;
    }

    public int getDefenseStability() {
        return defenseStability;
    }

    public int getProtection() {
        return protection;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean isCritical() {
        return critical;
    }
}
