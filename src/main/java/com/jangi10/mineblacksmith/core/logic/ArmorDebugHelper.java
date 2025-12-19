package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

import java.util.List;

/**
 * [방어구 디버그 헬퍼]
 * - 판금/사슬/안감 스탯 합산
 * - totalStrength / totalElasticity 산출
 * - ArmorLogic의 감쇄율/방어강도/보호수치까지 요약
 */
public class ArmorDebugHelper {

    public static String summarize(
            List<ForgingStats> plates,
            List<ForgingStats> chains,
            List<ForgingStats> linings,
            int usageCountForProtection
    ) {
        double totalStrength = ArmorPipeline.calculateTotalStrength(plates, chains, linings);
        double totalElasticity = ArmorPipeline.calculateTotalElasticity(plates, chains, linings);

        double ratio = ArmorLogic.calculateDamageReductionRatio(totalStrength, totalElasticity);
        int stability = ArmorLogic.calculateDefenseStability(totalStrength, totalElasticity);

        // 보호수치: 탄성 기준 + 사용량(예: 4부위면 4)
        int protection = ArmorLogic.calculateProtection(totalElasticity, usageCountForProtection);

        return ""
                + "[Armor Debug]\n"
                + "TotalStrength = " + totalStrength + "\n"
                + "TotalElasticity = " + totalElasticity + "\n"
                + "ReductionRatio = " + ratio + "\n"
                + "DefenseStability = " + stability + "\n"
                + "Protection = " + protection;
    }

    private ArmorDebugHelper() {}
}
