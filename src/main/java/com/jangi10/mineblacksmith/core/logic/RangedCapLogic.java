package com.jangi10.mineblacksmith.core.logic;

/**
 * [원거리 상한 로직]
 * - 발사력/차징속도/치명타 배율에 상한 적용
 * - 기획서 기준으로 숫자만 나중에 조정 가능
 */
public class RangedCapLogic {

    // 상한값(기획 확정 시 조정)
    private static final double MAX_POWER_MULTIPLIER = 3.0;   // 발사력 최대 300%
    private static final double MAX_SPEED_MULTIPLIER = 2.5;   // 차징속도 최대 250%
    private static final double MAX_CRIT_MULTIPLIER  = 2.0;   // 치명타 배율 최대 200%

    public static double capPower(double powerMul) {
        if (powerMul < 0) return 0;
        return Math.min(powerMul, MAX_POWER_MULTIPLIER);
    }

    public static double capSpeed(double speedMul) {
        if (speedMul < 0) return 0;
        return Math.min(speedMul, MAX_SPEED_MULTIPLIER);
    }

    public static double capCriticalMultiplier(double critMul) {
        if (critMul < 1.0) critMul = 1.0;
        return Math.min(critMul, MAX_CRIT_MULTIPLIER);
    }

    private RangedCapLogic() {}
}
