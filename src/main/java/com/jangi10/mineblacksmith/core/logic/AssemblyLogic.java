package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

import java.util.List;

public class AssemblyLogic {

    public static ForgingStats sumStats(List<ForgingStats> parts) {
        ForgingStats total = ForgingStats.empty();
        if (parts == null) return total;

        for (ForgingStats part : parts) {
            if (part != null) total = total.add(part);
        }

        return new ForgingStats(
                Math.max(1.0, total.getHardness()),
                Math.max(1.0, total.getDurability()),
                Math.max(1.0, total.getElasticity()),
                Math.max(1.0, total.getPolishing()),
                Math.max(1.0, total.getConductivity()),
                Math.max(0.1, total.getWeight())
        );
    }

    public static double calculateAttackDamage(double baseDamage, ForgingStats stats) {
        return calculateAttackDamage(baseDamage, stats, WeaponDamageType.SLASH);
    }

    public static double calculateAttackDamage(double baseDamage, ForgingStats stats, WeaponDamageType type) {
        if (stats == null) return baseDamage;

        double damage = baseDamage;

        switch (type) {
            case SLASH -> damage += stats.getHardness() * 0.6;
            case PIERCE -> damage += stats.getHardness() * 0.4 + stats.getConductivity() * 0.3;
            case BLUNT -> damage += stats.getHardness() * 0.8;
        }

        return damage;
    }

    public static double calculateRangedPower(double baseDamage, ForgingStats stats) {
        if (stats == null) return baseDamage;
        return baseDamage + (stats.getElasticity() * 0.5);
    }

    public static double calculateArmorReduction(ForgingStats stats) {
        if (stats == null) return 0;
        return Math.min(0.95, stats.getHardness() * 0.01);
    }

    private AssemblyLogic() {}
}
