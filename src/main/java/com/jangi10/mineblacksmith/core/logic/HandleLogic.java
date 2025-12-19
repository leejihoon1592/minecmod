package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

/**
 * 자루(HANDLE) 보정 로직
 * - 탄성: 공속
 * - 무게: 피해/조작성
 */
public class HandleLogic {

    private static final double HANDLE_AS_PER_ELASTIC = 0.006; // 탄성 1당 공속 +0.6%
    private static final double HANDLE_DAMAGE_PENALTY_PER_WEIGHT = 0.002; // 무게 1당 피해 -0.2%

    public static double getHandleAttackSpeedMultiplier(ForgingStats handleStats) {
        if (handleStats == null) return 1.0;
        return 1.0 + (handleStats.getElasticity() * HANDLE_AS_PER_ELASTIC);
    }

    public static double getHandleDamageMultiplier(ForgingStats handleStats) {
        if (handleStats == null) return 1.0;
        double mul = 1.0 - (handleStats.getWeight() * HANDLE_DAMAGE_PENALTY_PER_WEIGHT);
        return Math.max(0.10, mul);
    }

    private HandleLogic() {}
}
