package com.jangi10.mineblacksmith;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    // ✅ 아이템 레지스트리 (기초)
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MineBlacksmith.MODID);

    // ✅ NeoForge 1.21.x 필수: Item id 지정
    private static final ResourceKey<Item> FURNACE_CORE_ITEM_KEY =
            ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "furnace_core")
            );

    public static final DeferredHolder<Item, BlockItem> FURNACE_CORE_ITEM =
            ITEMS.register(
                    "furnace_core",
                    () -> new BlockItem(
                            ModBlocks.FURNACE_CORE.get(),
                            new Item.Properties().setId(FURNACE_CORE_ITEM_KEY)
                    )
            );

    private ModItems() {}
}
