package com.jangi10.mineblacksmith.core.init;

import com.jangi10.mineblacksmith.core.data.FuelData;
import com.jangi10.mineblacksmith.core.registry.FuelRegistry;

public class ModFuels {

    // FuelData 생성자: (id, name, targetItemId, min, max, limit)
    public static final FuelData WOOD =
            new FuelData("wood", "wood", "minecraft:oak_log", 600, 800, 1100);

    public static final FuelData CHARCOAL =
            new FuelData("charcoal", "charcoal", "minecraft:charcoal", 900, 1100, 1600);

    public static final FuelData COAL =
            new FuelData("coal", "coal", "minecraft:coal", 1000, 1200, 1900);

    public static final FuelData COKE =
            new FuelData("coke", "coke", "mineblacksmith:coke", 1100, 1300, 2000);

    // ✅ MineBlacksmith에서 호출할 메서드
    public static void registerAll() {
        FuelRegistry.register(WOOD);
        FuelRegistry.register(CHARCOAL);
        FuelRegistry.register(COAL);
        FuelRegistry.register(COKE);
    }

    /**
     * ✅ "바닐라 화로 연소시간" 컨셉으로 burnTicksTotal을 반환
     * - 지금 단계에서는 필요한 연료만 하드코딩(빠른 테스트 목적)
     * - coal/charcoal: 1600
     * - oak_log: 300 (바닐라 로그 계열 대표값)
     * - coke: 3200 (임의/확장값 — 필요하면 조정)
     *
     * itemId 예) "minecraft:coal"
     */
    public static int getBurnTicksForItemId(String itemId) {
        if (itemId == null || itemId.isBlank()) return 0;

        // 바닐라 대표값
        if (itemId.equals("minecraft:coal")) return 1600;
        if (itemId.equals("minecraft:charcoal")) return 1600;

        // 로그(대표)
        if (itemId.equals("minecraft:oak_log")) return 300;

        // 우리 연료
        if (itemId.equals("mineblacksmith:coke")) return 3200;

        // 등록 안 된 연료는 0 (점화 불가)
        return 0;
    }

    private ModFuels() {}
}


