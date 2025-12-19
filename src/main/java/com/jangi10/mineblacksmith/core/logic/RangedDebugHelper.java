package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.RangedCombatResult;

/**
 * [원거리 디버그 헬퍼]
 * - 현재 스탯으로 어떤 값이 나오는지 문자열로 요약
 * - 콘솔/로그/개발용 UI에서 사용
 */
public class RangedDebugHelper {

    public static String summarize(
            double baseDamage,
            double baseCritPercent,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double targetStrength,
            double targetElasticity,
            int targetProtection
    ) {
        RangedCombatResult result = RangedPipeline.run(
                baseDamage,
                baseCritPercent,
                limbStats,
                stringStats,
                targetStrength,
                targetElasticity,
                targetProtection
        );

        return ""
                + "[Ranged Debug]\n"
                + "FinalDamage = " + result.getFinalDamage() + "\n"
                + "ProjectilePowerMul = " + result.getProjectilePowerMultiplier() + "\n"
                + "ChargeSpeedMul = " + result.getChargeSpeedMultiplier() + "\n"
                + "Critical = " + result.isCritical();
    }

    private RangedDebugHelper() {}
}
