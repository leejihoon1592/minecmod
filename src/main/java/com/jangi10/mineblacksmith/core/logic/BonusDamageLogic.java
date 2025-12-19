package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

/**
 * 증댐(연마 기반) + 상한
 * - 상한은 무기군/등급/세공에 따라 나중에 확장 가능
 */
public class BonusDamageLogic {

    private static final double BONUS_PER_POLISH = 0.05; // 연마 1당 +0.05
    private static final double BONUS_CAP = 25.0;        // 기본 상한(기획서 확정 시 조정)

    public static double calculateBonusDamage(ForgingStats stats) {
        if (stats == null) return 0.0;
        double bonus = stats.getPolishing() * BONUS_PER_POLISH;
        if (bonus < 0) bonus = 0;
        if (bonus > BONUS_CAP) bonus = BONUS_CAP;
        return bonus;
    }

    private BonusDamageLogic() {}
}
