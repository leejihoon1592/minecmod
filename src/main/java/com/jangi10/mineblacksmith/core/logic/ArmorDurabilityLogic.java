package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

/**
 * [방어구 내구 소모 로직]
 * - 판금: 피해량에 가장 크게 반응
 * - 사슬: 안정성에 반응
 * - 안감: 소량의 고정 소모
 *
 * ⚠️ 실제 아이템 내구 감소는 외부(아이템 로직)에서 처리
 */
public class ArmorDurabilityLogic {

    private static final int BASE_PLATE_WEAR = 1;
    private static final int BASE_CHAIN_WEAR = 1;
    private static final int BASE_LINING_WEAR = 1;

    /**
     * 판금 내구 소모
     */
    public static int calculatePlateWear(double incomingDamage, ForgingStats plateStats) {
        double wear = BASE_PLATE_WEAR + (incomingDamage * 0.02);
        return Math.max(1, (int) Math.ceil(wear));
    }

    /**
     * 사슬 내구 소모
     */
    public static int calculateChainWear(int defenseStability, ForgingStats chainStats) {
        double wear = BASE_CHAIN_WEAR + (defenseStability * 0.3);
        return Math.max(1, (int) Math.ceil(wear));
    }

    /**
     * 안감 내구 소모
     */
    public static int calculateLiningWear(ForgingStats liningStats) {
        return BASE_LINING_WEAR;
    }

    private ArmorDurabilityLogic() {}
}
