package com.jangi10.mineblacksmith.core.logic;

/**
 * [전투 스냅샷]
 * - 근접/원거리 공통
 * - 서버 로그, 리플레이, 밸런스 검증용
 */
public class CombatSnapshot {

    private final double baseDamage;
    private final double finalDamage;
    private final boolean critical;

    private final double reductionRatio;
    private final int defenseStability;
    private final int protection;

    private final double speedMultiplier; // 근접 공속/이동속도 or 원거리 차징/발사 혼합

    public CombatSnapshot(double baseDamage,
                          double finalDamage,
                          boolean critical,
                          double reductionRatio,
                          int defenseStability,
                          int protection,
                          double speedMultiplier) {
        this.baseDamage = baseDamage;
        this.finalDamage = finalDamage;
        this.critical = critical;
        this.reductionRatio = reductionRatio;
        this.defenseStability = defenseStability;
        this.protection = protection;
        this.speedMultiplier = speedMultiplier;
    }

    public double getBaseDamage() { return baseDamage; }
    public double getFinalDamage() { return finalDamage; }
    public boolean isCritical() { return critical; }
    public double getReductionRatio() { return reductionRatio; }
    public int getDefenseStability() { return defenseStability; }
    public int getProtection() { return protection; }
    public double getSpeedMultiplier() { return speedMultiplier; }
}
