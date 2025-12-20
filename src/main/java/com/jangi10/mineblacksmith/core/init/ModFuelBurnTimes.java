package com.jangi10.mineblacksmith.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public final class ModFuelBurnTimes {

    // ✅ 여기만 너 기획대로 바꾸면 됨
    // - 석탄: 1600
    // - 코크스는 석탄 상위로 (예: 3200)
    public static final int COKE_BURN_TICKS = 3200;
    public static final int COKE_BLOCK_BURN_TICKS = COKE_BURN_TICKS * 9;

    private static final String ID_COKE = "mineblacksmith:coke";
    private static final String ID_COKE_BLOCK_ITEM = "mineblacksmith:coke_block";

    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        if (ID_COKE.equals(id)) {
            event.setBurnTime(COKE_BURN_TICKS);
            return;
        }

        if (ID_COKE_BLOCK_ITEM.equals(id)) {
            event.setBurnTime(COKE_BLOCK_BURN_TICKS);
        }
    }

    private ModFuelBurnTimes() {}
}
