package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

import java.util.List;

/**
 * [방어구 파이프라인]
 * - 판금/사슬/안감 스탯을 받아
 * - 방어 계산에 사용할 "총합 강도/탄성"을 산출
 */
public class ArmorPipeline {

    /**
     * 방어용 강도 총합 계산
     */
    public static double calculateTotalStrength(
            List<ForgingStats> plates,
            List<ForgingStats> chains,
            List<ForgingStats> linings
    ) {
        double s = 0;
        s += ArmorAssemblyLogic.sumPlates(plates).getHardness();
        s += ArmorAssemblyLogic.sumChains(chains).getHardness() * 0.5; // 사슬은 절반만 강도 기여
        s += ArmorAssemblyLogic.sumLinings(linings).getHardness() * 0.2;
        return s;
    }

    /**
     * 방어용 탄성 총합 계산
     */
    public static double calculateTotalElasticity(
            List<ForgingStats> plates,
            List<ForgingStats> chains,
            List<ForgingStats> linings
    ) {
        double e = 0;
        e += ArmorAssemblyLogic.sumPlates(plates).getElasticity() * 0.3;
        e += ArmorAssemblyLogic.sumChains(chains).getElasticity();
        e += ArmorAssemblyLogic.sumLinings(linings).getElasticity() * 0.5;
        return e;
    }

    private ArmorPipeline() {}
}
