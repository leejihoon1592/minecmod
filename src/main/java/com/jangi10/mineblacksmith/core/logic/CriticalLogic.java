package com.jangi10.mineblacksmith.core.logic;

/**
 * 치명타 판정 로직
 * - chancePercent: 0~100
 */
public class CriticalLogic {

    public static boolean rollCritical(double chancePercent) {
        if (chancePercent <= 0) return false;
        if (chancePercent >= 100) return true;
        return Math.random() * 100.0 < chancePercent;
    }

    private CriticalLogic() {}
}
