package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.CombatSnapshot;

/**
 * [전투 안전성 검사]
 * - NaN / 음수 / 비정상 값 방지
 * - 개발 단계에서 밸런스 붕괴 조기 발견
 */
public class CombatSanityChecker {

    public static void validate(CombatSnapshot s) {
        if (s == null) {
            throw new IllegalStateException("CombatSnapshot is null");
        }

        check("baseDamage", s.getBaseDamage());
        check("finalDamage", s.getFinalDamage());
        check("speedMultiplier", s.getSpeedMultiplier());

        if (s.getFinalDamage() < 0) {
            throw new IllegalStateException("finalDamage < 0");
        }
        if (s.getSpeedMultiplier() <= 0) {
            throw new IllegalStateException("speedMultiplier <= 0");
        }
    }

    private static void check(String name, double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) {
            throw new IllegalStateException(name + " is invalid: " + v);
        }
    }

    private CombatSanityChecker() {}
}
