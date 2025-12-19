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

    private ModFuels() {}
}


