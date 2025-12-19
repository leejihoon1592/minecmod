package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

/**
 * 치명타 확률 계산
 * - 기본 치명타율 + 연마/강도 기반 보정
 * - 반환값: 0~100 (%)
 */
public class CriticalChanceLogic {

    public static double calculateChance(double baseCritPercent, ForgingStats stats) {
        if (stats == null) return clamp(baseCritPercent);

        double bonus = (stats.getPolishing() * 0.15) + (stats.getHardness() * 0.05);
        return clamp(baseCritPercent + bonus);
    }

    private static double clamp(double v) {
        if (v < 0) return 0;
        if (v > 100) return 100;
        return v;
    }

    private CriticalChanceLogic() {}
}
