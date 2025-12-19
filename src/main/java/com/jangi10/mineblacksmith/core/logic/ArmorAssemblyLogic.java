package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.ForgingStats;

import java.util.List;

/**
 * [방어구 조립 로직]
 * - 판금(ARMOR_PLATE): 강도 중심, 감쇄율↑
 * - 사슬(ARMOR_CHAIN): 탄성 중심, 안정성↑
 * - 안감(LINING): 연마/무게 중심, 착용 페널티↓
 *
 * ⚠️ 부위 분류는 아이템 레벨에서 처리하고
 * 코어는 "분류된 스탯 리스트"를 받아 합산만 한다.
 */
public class ArmorAssemblyLogic {

    /** 판금 합산 */
    public static ForgingStats sumPlates(List<ForgingStats> plates) {
        return AssemblyLogic.sumStats(plates);
    }

    /** 사슬 합산 */
    public static ForgingStats sumChains(List<ForgingStats> chains) {
        return AssemblyLogic.sumStats(chains);
    }

    /** 안감 합산 */
    public static ForgingStats sumLinings(List<ForgingStats> linings) {
        return AssemblyLogic.sumStats(linings);
    }

    private ArmorAssemblyLogic() {}
}
