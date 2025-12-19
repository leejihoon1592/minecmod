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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEBLACKSMITH_TAB =
            CREATIVE_MODE_TABS.register("mineblacksmith", () ->
                    CreativeModeTab.builder()
                            // 탭 이름(번역키)
                            .title(Component.translatable("itemGroup." + MineBlacksmith.MODID + ".mineblacksmith"))
                            // 탭 아이콘(지금은 furnace_core로)
                            .icon(() -> new ItemStack(ModItems.FURNACE_CORE_ITEM.get()))
                            // 탭에 표시될 아이템들
                            .displayItems((params, output) -> {
                                output.accept(ModItems.FURNACE_CORE_ITEM.get());
                                output.accept(ModItems.COKE.get());
                                output.accept(ModItems.COKE_BLOCK_ITEM.get());
                                // 나중에 아이템/블록 늘어나면 여기 계속 추가

                            })
                            .build()
            );

    private ModCreativeTabs() {}
}
