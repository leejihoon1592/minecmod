package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

import java.util.List;

/**
 * [원거리 조립(활) 헬퍼]
 * - 활대(BOW_LIMB) 여러 개(복합 활 등) 합산
 * - 활시위(BOW_STRING) 합산
 *
 * ⚠️ 부품 분류(WeaponPartType)까지는 아이템 레벨에서 처리하고,
 * 코어는 "이미 분류된 스탯 리스트"를 합산만 한다.
 */
public class RangedAssemblyLogic {

    public static ForgingStats sumLimbs(List<ForgingStats> limbParts) {
        return AssemblyLogic.sumStats(limbParts);
    }

    public static ForgingStats sumStrings(List<ForgingStats> stringParts) {
        return AssemblyLogic.sumStats(stringParts);
    }

    private RangedAssemblyLogic() {}
}
