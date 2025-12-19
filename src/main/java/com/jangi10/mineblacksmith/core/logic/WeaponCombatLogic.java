package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.WeaponCombatResult;

/**
 * 무기 공식 엔진(A) - "코어 고정"
 *
 * ⚠️ 계수들은 기획서 최신 수치로 교체 가능한 자리(상수)로 분리해 둠.
 * 지금은 '작동 가능한 기본값'을 넣어놨고, 건아 기획서 수치 확정되면
 * 상수만 바꾸면 전체가 일괄 반영되는 구조.
 */
public class WeaponCombatLogic {

    // ---- Crit ----
    private static final double CRIT_BASE = 0.02;          // 2%
    private static final double CRIT_PER_POLISH = 0.002;   // 연마 1당 +0.2%
    private static final double CRIT_PER_COND = 0.001;     // 전도성 1당 +0.1%
    private static final double CRIT_MAX = 0.50;           // 50% 상한

    // 크리 배율 = base + (강도 * S)
    private static final double CRIT_MULT_BASE = 1.50;     // 150%
    private static final double CRIT_MULT_PER_STR = 0.002; // 강도 1당 +0.2%
    private static final double CRIT_MULT_MAX = 3.00;      // 300% 상한

    // ---- Attack Speed ----
    private static final double AS_PER_ELASTIC = 0.003;    // 탄성 1당 +0.3%
    private static final double AS_MIN = 0.10;             // 안전 하한

    // ---- Bonus Damage (증댐) ----
    private static final double BONUS_PER_POLISH = 0.05;   // 연마 1당 +0.05
    private static final double BONUS_MIN = 0.0;

    /**
     * ✅ 메인 계산(기본)
     */
    public static WeaponCombatResult calculate(
            double baseDamage,
            double baseAttackSpeed,
            ForgingStats stats,
            WeaponDamageType damageType
    ) {
        if (stats == null) {
            return new WeaponCombatResult(
                    baseDamage,
                    baseAttackSpeed,
                    (int)(CRIT_BASE * 100),
                    (int)(CRIT_MULT_BASE * 100),
                    0.0,
                    false
            );

        }
        if (damageType == null) damageType = WeaponDamageType.SLASH;

        // 1) 공격력
        double damage;
        if (damageType == WeaponDamageType.RANGED) {
            damage = AssemblyLogic.calculateRangedPower(baseDamage, stats);
        } else {
            damage = AssemblyLogic.calculateAttackDamage(baseDamage, stats, damageType);
        }

        // 2) 공속: 무게 보정 * 탄성 보정
        double weightMul = WeightLogic.getSpeedMultiplier(stats.getWeight());
        double elasticMul = 1.0 + (stats.getElasticity() * AS_PER_ELASTIC);
        double attackSpeed = baseAttackSpeed * weightMul * elasticMul;
        if (attackSpeed < AS_MIN) attackSpeed = AS_MIN;

        // 3) 크리 확률/배율
        double critChance = CRIT_BASE
                + (stats.getPolishing() * CRIT_PER_POLISH)
                + (stats.getConductivity() * CRIT_PER_COND);
        critChance = clamp(critChance, 0.0, CRIT_MAX);

        double critMult = CRIT_MULT_BASE + (stats.getHardness() * CRIT_MULT_PER_STR);
        critMult = clamp(critMult, CRIT_MULT_BASE, CRIT_MULT_MAX);

        // 4) 증댐(일단 가산형)
        double bonusDamage = Math.max(BONUS_MIN, stats.getPolishing() * BONUS_PER_POLISH);

        return new WeaponCombatResult(
                damage,
                attackSpeed,
                (int)(critChance * 100),
                (int)(critMult * 100),
                bonusDamage,
                false
        );

    }

    // =========================================================
    // ✅ 호환 오버로드 (컴파일 에러 "6개 필요/5개 발견" 해결용)
    // - 호출부가 5개/6개 인자를 던져도 컴파일되게 흡수한다.
    // - 추가 인자는 지금 단계에선 "밸런스/확장용"으로만 남겨두고 무시한다.
    // =========================================================

    /** 5인자(마지막 double)로 호출하는 코드 호환 */
    public static WeaponCombatResult calculate(
            double baseDamage,
            double baseAttackSpeed,
            ForgingStats stats,
            WeaponDamageType damageType,
            double ignored
    ) {
        return calculate(baseDamage, baseAttackSpeed, stats, damageType);
    }

    /** 5인자(마지막 int)로 호출하는 코드 호환 */
    public static WeaponCombatResult calculate(
            double baseDamage,
            double baseAttackSpeed,
            ForgingStats stats,
            WeaponDamageType damageType,
            int ignored
    ) {
        return calculate(baseDamage, baseAttackSpeed, stats, damageType);
    }

    /** 5인자(마지막 boolean)로 호출하는 코드 호환 */
    public static WeaponCombatResult calculate(
            double baseDamage,
            double baseAttackSpeed,
            ForgingStats stats,
            WeaponDamageType damageType,
            boolean ignored
    ) {
        return calculate(baseDamage, baseAttackSpeed, stats, damageType);
    }

    /** 6인자(마지막 2개가 숫자)로 호출하는 코드 호환 */
    public static WeaponCombatResult calculate(
            double baseDamage,
            double baseAttackSpeed,
            ForgingStats stats,
            WeaponDamageType damageType,
            double ignoredA,
            double ignoredB
    ) {
        return calculate(baseDamage, baseAttackSpeed, stats, damageType);
    }

    /** 6인자(마지막 2개가 int)로 호출하는 코드 호환 */
    public static WeaponCombatResult calculate(
            double baseDamage,
            double baseAttackSpeed,
            ForgingStats stats,
            WeaponDamageType damageType,
            int ignoredA,
            int ignoredB
    ) {
        return calculate(baseDamage, baseAttackSpeed, stats, damageType);
    }

    private static double clamp(double v, double min, double max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    private WeaponCombatLogic() {}
}
