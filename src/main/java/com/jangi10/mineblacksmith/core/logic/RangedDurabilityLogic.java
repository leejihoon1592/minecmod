package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

/**
 * [원거리 부위별 내구 소모]
 * - 활대(BOW_LIMB): 발사 시 기본 소모
 * - 활시위(BOW_STRING): 차징/치명타 시 추가 소모
 *
 * 반환값은 "소모량(정수)"이며,
 * 실제 아이템 내구 감소는 외부(아이템 로직)에서 처리.
 */
public class RangedDurabilityLogic {

    // 기본 소모량
    private static final int LIMB_BASE_WEAR = 1;
    private static final int STRING_BASE_WEAR = 1;

    // 보정 계수
    private static final double WEAR_PER_POWER = 0.02;   // 발사력 배율당 소모 증가
    private static final double WEAR_PER_SPEED = 0.01;   // 차징 속도 배율당 소모 증가

    /**
     * 활대 내구 소모 계산
     */
    public static int calculateLimbWear(double projectilePowerMultiplier, ForgingStats limbStats) {
        double wear = LIMB_BASE_WEAR + (projectilePowerMultiplier * WEAR_PER_POWER);
        return Math.max(1, (int) Math.ceil(wear));
    }

    /**
     * 활시위 내구 소모 계산
     */
    public static int calculateStringWear(double chargeSpeedMultiplier, boolean critical, ForgingStats stringStats) {
        double wear = STRING_BASE_WEAR + (chargeSpeedMultiplier * WEAR_PER_SPEED);
        if (critical) wear += 1; // 치명타 시 추가 소모
        return Math.max(1, (int) Math.ceil(wear));
    }

    private RangedDurabilityLogic() {}
}
