package com.jangi10.mineblacksmith;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    // CreativeModeTab도 레지스트리라서 DeferredRegister로 등록
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MineBlacksmith.MODID);

    /**
     * 1) MineBlacksmith Function
     *  - 기계/기능 블록/도구/가이드북 등
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEBLACKSMITH_FUNCTION_TAB =
            CREATIVE_MODE_TABS.register("mineblacksmith_function", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup." + MineBlacksmith.MODID + ".mineblacksmith_function"))
                            .icon(() -> new ItemStack(ModItems.RESEARCH_CODEX.get()))
                            .displayItems((params, output) -> {

                                // 가이드북(항상 1번)
                                output.accept(ModItems.RESEARCH_CODEX.get());

                                // 기능 블록/아이템
                                output.accept(ModItems.FURNACE_CORE_ITEM.get());
                                output.accept(ModItems.COKE_OVEN_ITEM.get());

                                // 연료/재료(기능 탭에도 편의상 노출)
                                output.accept(ModItems.COKE.get());
                                output.accept(ModItems.COKE_BLOCK_ITEM.get());

                            })
                            .build()
            );

    /**
     * 2) MineBlacksmith Material
     *  - 원석/주괴/조각/재료 계열
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEBLACKSMITH_MATERIAL_TAB =
            CREATIVE_MODE_TABS.register("mineblacksmith_material", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup." + MineBlacksmith.MODID + ".mineblacksmith_material"))
                            .icon(() -> new ItemStack(ModItems.INGOTS.get("accutyrone").get()))
                            .displayItems((params, output) -> {
                                for (String id : ModItems.METAL_IDS) {
                                    output.accept(ModItems.RAWS.get(id).get());
                                    output.accept(ModItems.INGOTS.get(id).get());
                                    output.accept(ModItems.NUGGETS.get(id).get());
                                }
                            })
                            .build()
            );

    private ModCreativeTabs() {}
}
