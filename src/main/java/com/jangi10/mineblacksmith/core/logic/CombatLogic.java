package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.WeaponCombatResult;

public class CombatLogic {
    /**
     * [원거리 - 결과 분리형]
     * - 활대/활시위로 발사력/차징 속도를 각각 계산
     * - 방어/보호수치까지 반영한 최종 피해 산출
     */
    public static com.jangi10.mineblacksmith.core.data.RangedCombatResult calculateRangedCombatResult(
            double baseDamage,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double totalStrength,
            double totalElasticity,
            int protection,
            boolean critical
    ) {
        double powerMul = RangedLogic.getRangedPowerMultiplier(limbStats, stringStats);
        double chargeMul = RangedLogic.getRangedSpeedMultiplier(limbStats, stringStats);

        double damage = baseDamage * powerMul;
        if (critical) damage *= 1.5;

        double reductionRatio = ArmorLogic.calculateDamageReductionRatio(totalStrength, totalElasticity);
        double afterArmor = damage * (1.0 - reductionRatio);

        double protMul = 1.0 - Math.min(20, Math.max(0, protection)) * 0.01;
        if (protMul < 0) protMul = 0;

        double finalDamage = afterArmor * protMul;

        return new com.jangi10.mineblacksmith.core.data.RangedCombatResult(
                finalDamage,
                chargeMul,
                powerMul,
                critical
        );
    }


    // 기존: 근접(자루 없음)
    public static WeaponCombatResult calculateCombat(
            double baseDamage,
            ForgingStats stats,
            WeaponDamageType damageType,
            double totalStrength,
            double totalElasticity,
            int protection,
            double currentWeightKg,
            boolean critical
    ) {
        double damage = AssemblyLogic.calculateAttackDamage(baseDamage, stats, damageType);
        if (critical) damage *= 1.5;

        double reductionRatio = ArmorLogic.calculateDamageReductionRatio(totalStrength, totalElasticity);
        int stability = ArmorLogic.calculateDefenseStability(totalStrength, totalElasticity);
        double afterArmor = damage * (1.0 - reductionRatio);

        double protMul = 1.0 - Math.min(20, Math.max(0, protection)) * 0.01;
        if (protMul < 0) protMul = 0;

        double finalDamage = afterArmor * protMul;
        double speedMul = WeightLogic.getSpeedMultiplier(currentWeightKg);

        return new WeaponCombatResult(
                finalDamage,
                reductionRatio,
                stability,
                protection,
                speedMul,
                critical
        );
    }

    // 기존: 근접(자루 포함)
    public static WeaponCombatResult calculateCombatWithHandle(
            double baseDamage,
            ForgingStats coreStats,
            ForgingStats handleStats,
            WeaponDamageType damageType,
            double totalStrength,
            double totalElasticity,
            int protection,
            double currentWeightKg,
            boolean critical
    ) {
        double damage = AssemblyLogic.calculateAttackDamage(baseDamage, coreStats, damageType);

        damage *= HandleLogic.getHandleDamageMultiplier(handleStats);
        damage += BonusDamageLogic.calculateBonusDamage(coreStats);

        if (critical) damage *= 1.5;

        double reductionRatio = ArmorLogic.calculateDamageReductionRatio(totalStrength, totalElasticity);
        int stability = ArmorLogic.calculateDefenseStability(totalStrength, totalElasticity);
        double afterArmor = damage * (1.0 - reductionRatio);

        double protMul = 1.0 - Math.min(20, Math.max(0, protection)) * 0.01;
        if (protMul < 0) protMul = 0;

        double finalDamage = afterArmor * protMul;

        double speedMul = WeightLogic.getSpeedMultiplier(currentWeightKg);
        speedMul *= HandleLogic.getHandleAttackSpeedMultiplier(handleStats);

        return new WeaponCombatResult(
                finalDamage,
                reductionRatio,
                stability,
                protection,
                speedMul,
                critical
        );
    }

    // 신규: 원거리(활대/활시위 포함)
    public static WeaponCombatResult calculateRangedCombat(
            double baseDamage,
            ForgingStats limbStats,      // BOW_LIMB
            ForgingStats stringStats,    // BOW_STRING
            double totalStrength,
            double totalElasticity,
            int protection,
            double currentWeightKg,
            boolean critical
    ) {
        // 1) 원거리 기본 발사력(기존 공식 재사용 가능)
        // RANGED는 enum에 있지만, 실제 원거리 공식은 별도라 limb/string 기반으로 보정한다.
        double damage = baseDamage;

        // 2) 발사력 보정(활대/시위)
        damage *= RangedLogic.getRangedPowerMultiplier(limbStats, stringStats);

        // 3) 치명타(단순 배율)
        if (critical) damage *= 1.5;

        // 4) 방어(일단 동일 파이프라인 적용)
        double reductionRatio = ArmorLogic.calculateDamageReductionRatio(totalStrength, totalElasticity);
        int stability = ArmorLogic.calculateDefenseStability(totalStrength, totalElasticity);
        double afterArmor = damage * (1.0 - reductionRatio);

        // 5) 보호수치 후처리
        double protMul = 1.0 - Math.min(20, Math.max(0, protection)) * 0.01;
        if (protMul < 0) protMul = 0;

        double finalDamage = afterArmor * protMul;

        // 6) 속도(이동속도는 무게, 발사/차징은 rangedSpeedMul로 "결과 speedMultiplier"에 합성)
        double moveSpeedMul = WeightLogic.getSpeedMultiplier(currentWeightKg);
        double rangedSpeedMul = RangedLogic.getRangedSpeedMultiplier(limbStats, stringStats);

        return new WeaponCombatResult(
                finalDamage,
                reductionRatio,
                stability,
                protection,
                moveSpeedMul * rangedSpeedMul,
                critical
        );
    }

    private CombatLogic() {}
}
