package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.RangedCombatResult;

/**
 * [원거리 내구 파이프라인]
 * - 전투 결과(RangedCombatResult)를 입력으로 받아
 * - 활대(BOW_LIMB) / 활시위(BOW_STRING) 내구 소모를 한 번에 계산
 *
 * ⚠️ 실제 아이템 내구 감소는 외부(아이템 로직)에서 처리
 */
public class RangedDurabilityPipeline {

    /**
     * 활대 내구 소모 계산
     */
    public static int calcLimbWear(
            RangedCombatResult result,
            ForgingStats limbStats
    ) {
        if (result == null) return 1;
        return RangedDurabilityLogic.calculateLimbWear(
                result.getProjectilePowerMultiplier(),
                limbStats
        );
    }

    /**
     * 활시위 내구 소모 계산
     */
    public static int calcStringWear(
            RangedCombatResult result,
            ForgingStats stringStats
    ) {
        if (result == null) return 1;
        return RangedDurabilityLogic.calculateStringWear(
                result.getChargeSpeedMultiplier(),
                result.isCritical(),
                stringStats
        );
    }

    private RangedDurabilityPipeline() {}
}
