package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.RangedCombatResult;

/**
 * [원거리 파이프라인]
 * - 치명타 굴림
 * - 상한 적용
 * - 최종 결과 산출
 */
public class RangedPipeline {

    public static RangedCombatResult run(
            double baseDamage,
            double baseCritPercent,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double totalStrength,
            double totalElasticity,
            int protection
    ) {
        // 1) 치명타 확률 계산
        double critChance = baseCritPercent + RangedLogic.getRangedCritBonusPercent(limbStats, stringStats);
        if (critChance < 0) critChance = 0;
        if (critChance > 100) critChance = 100;

        boolean crit = CriticalLogic.rollCritical(critChance);

        // 2) 발사력/차징속도 계산 + 상한
        double rawPower = RangedLogic.getRangedPowerMultiplier(limbStats, stringStats);
        double rawSpeed = RangedLogic.getRangedSpeedMultiplier(limbStats, stringStats);

        double powerMul = RangedCapLogic.capPower(rawPower);
        double speedMul = RangedCapLogic.capSpeed(rawSpeed);

        // 3) 피해 계산
        double damage = baseDamage * powerMul;

        double critMul = crit ? RangedCapLogic.capCriticalMultiplier(1.5) : 1.0;
        damage *= critMul;

        // 4) 방어/보호
        double reductionRatio = ArmorLogic.calculateDamageReductionRatio(totalStrength, totalElasticity);
        double afterArmor = damage * (1.0 - reductionRatio);

        double protMul = 1.0 - Math.min(20, Math.max(0, protection)) * 0.01;
        if (protMul < 0) protMul = 0;

        double finalDamage = afterArmor * protMul;

        return new RangedCombatResult(
                finalDamage,
                speedMul,
                powerMul,
                crit
        );
    }

    private RangedPipeline() {}
}
