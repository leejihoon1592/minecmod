package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.WeaponCombatResult;

import java.util.List;

/**
 * [방어구 내구 파이프라인]
 * - WeaponCombatResult 기반으로
 * - 판금/사슬/안감 내구 소모를 산출
 */
public class ArmorDurabilityPipeline {

    public static int calculatePlateWear(
            WeaponCombatResult result,
            List<ForgingStats> plates
    ) {
        if (result == null) return 1;
        return ArmorDurabilityLogic.calculatePlateWear(
                result.getFinalDamage(),
                ArmorAssemblyLogic.sumPlates(plates)
        );
    }

    public static int calculateChainWear(
            WeaponCombatResult result,
            List<ForgingStats> chains
    ) {
        if (result == null) return 1;
        return ArmorDurabilityLogic.calculateChainWear(
                result.getDefenseStability(),
                ArmorAssemblyLogic.sumChains(chains)
        );
    }

    public static int calculateLiningWear(
            List<ForgingStats> linings
    ) {
        return ArmorDurabilityLogic.calculateLiningWear(
                ArmorAssemblyLogic.sumLinings(linings)
        );
    }

    private ArmorDurabilityPipeline() {}
}
