package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

/**
 * 원거리(활/석궁) 전용 보정 엔진
 * - limbStats: 활대(BOW_LIMB) 스탯
 * - stringStats: 활시위(BOW_STRING) 스탯
 *
 * ⚠️ 계수는 기획서 확정 시 상수만 수정
 */
public class RangedLogic {

    // 발사력(피해) 보정: 탄성 중심
    private static final double POWER_PER_ELASTIC = 0.008;     // 탄성 1당 +0.8%
    private static final double POWER_PER_HARDNESS = 0.002;    // 강도 1당 +0.2%
    private static final double POWER_WEIGHT_PENALTY = 0.002;  // 무게 1당 -0.2%

    // 차징/발사 속도 보정: 연마/탄성 중심
    private static final double SPEED_PER_POLISH = 0.006;      // 연마 1당 +0.6%
    private static final double SPEED_PER_ELASTIC = 0.003;     // 탄성 1당 +0.3%
    private static final double SPEED_WEIGHT_PENALTY = 0.0015; // 무게 1당 -0.15%

    // 치명타(원거리): 연마 + 전도성
    private static final double CRIT_PER_POLISH = 0.10;        // 연마 1당 +0.10%
    private static final double CRIT_PER_COND = 0.05;          // 전도성 1당 +0.05%

    public static double getRangedPowerMultiplier(ForgingStats limbStats, ForgingStats stringStats) {
        ForgingStats l = limbStats;
        ForgingStats s = stringStats;

        double elastic = (l == null ? 0 : l.getElasticity()) + (s == null ? 0 : s.getElasticity());
        double hardness = (l == null ? 0 : l.getHardness()) + (s == null ? 0 : s.getHardness());
        double weight = (l == null ? 0 : l.getWeight()) + (s == null ? 0 : s.getWeight());

        double mul = 1.0
                + elastic * POWER_PER_ELASTIC
                + hardness * POWER_PER_HARDNESS
                - weight * POWER_WEIGHT_PENALTY;

        return Math.max(0.10, mul);
    }

    public static double getRangedSpeedMultiplier(ForgingStats limbStats, ForgingStats stringStats) {
        ForgingStats l = limbStats;
        ForgingStats s = stringStats;

        double polish = (l == null ? 0 : l.getPolishing()) + (s == null ? 0 : s.getPolishing());
        double elastic = (l == null ? 0 : l.getElasticity()) + (s == null ? 0 : s.getElasticity());
        double weight = (l == null ? 0 : l.getWeight()) + (s == null ? 0 : s.getWeight());

        double mul = 1.0
                + polish * SPEED_PER_POLISH
                + elastic * SPEED_PER_ELASTIC
                - weight * SPEED_WEIGHT_PENALTY;

        return Math.max(0.10, mul);
    }

    /** 원거리 치명타 확률(%) 보정값 */
    public static double getRangedCritBonusPercent(ForgingStats limbStats, ForgingStats stringStats) {
        ForgingStats l = limbStats;
        ForgingStats s = stringStats;

        double polish = (l == null ? 0 : l.getPolishing()) + (s == null ? 0 : s.getPolishing());
        double cond = (l == null ? 0 : l.getConductivity()) + (s == null ? 0 : s.getConductivity());

        return (polish * CRIT_PER_POLISH) + (cond * CRIT_PER_COND);
    }

    private RangedLogic() {}
}
