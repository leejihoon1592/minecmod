package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.core.init.BlacksmithCoreBootstrap;
import com.jangi10.mineblacksmith.core.init.ModFuelBurnTimes;
import com.jangi10.mineblacksmith.core.init.ModFuels;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MineBlacksmith.MODID)
public class MineBlacksmith {
    public static final String MODID = "mineblacksmith";

    public MineBlacksmith(IEventBus modBus) {

        // =========================
        // Common init (데이터/태그/연료 등)
        // =========================
        ModFuels.registerAll();
        NeoForge.EVENT_BUS.addListener(ModFuelBurnTimes::onFuelBurnTime);

        // =========================
        // Core bootstrap (config-driven data)
        // - fuels / ingots / ores
        // =========================
        modBus.addListener((FMLCommonSetupEvent evt) -> evt.enqueueWork(() ->
                BlacksmithCoreBootstrap.bootstrap(FMLPaths.CONFIGDIR.get().toFile())
        ));

        // =========================
        // Mod registries (반드시 "한 번만")
        // =========================
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);

        // ✅ 크리에이티브 탭 2개(Function/Material) 등록 (한 번만)
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modBus);
    }
}

