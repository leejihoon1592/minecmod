package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.CombatSnapshot;
import com.jangi10.mineblacksmith.core.data.RangedCombatResult;
import com.jangi10.mineblacksmith.core.data.WeaponCombatResult;

/**
 * [전투 스냅샷 매퍼]
 * 결과 객체 → CombatSnapshot
 */
public class CombatSnapshotMapper {

    /** 근접/일반 */
    public static CombatSnapshot fromWeaponResult(
            double baseDamage,
            WeaponCombatResult r
    ) {
        return new CombatSnapshot(
                baseDamage,
                r.getFinalDamage(),
                r.isCritical(),
                r.getArmorReductionRatio(),
                r.getDefenseStability(),
                r.getProtection(),
                r.getSpeedMultiplier()
        );
    }

    /** 원거리 */
    public static CombatSnapshot fromRangedResult(
            double baseDamage,
            RangedCombatResult r
    ) {
        return new CombatSnapshot(
                baseDamage,
                r.getFinalDamage(),
                r.isCritical(),
                0.0,          // 원거리는 방어 세부를 외부에서 처리 가능
                0,
                0,
                r.getChargeSpeedMultiplier() * r.getProjectilePowerMultiplier()
        );
    }

    private CombatSnapshotMapper() {}
}
