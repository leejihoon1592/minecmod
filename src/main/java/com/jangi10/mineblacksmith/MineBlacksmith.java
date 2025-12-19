package com.jangi10.mineblacksmith;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MineBlacksmith.MODID)
public class MineBlacksmith {
    public static final String MODID = "mineblacksmith";

    public MineBlacksmith(IEventBus modBus) {

        com.jangi10.mineblacksmith.core.init.ModFuels.registerAll();

        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);

        // ✅ 이 줄 추가
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modBus);

    }
}
