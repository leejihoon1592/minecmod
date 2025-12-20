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

    // =========================
    // furnace_core (BlockItem)
    // =========================
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

    // =========================
    // coke (Item)
    // =========================
    private static final ResourceKey<Item> COKE_ITEM_KEY =
            ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "coke")
            );

    public static final DeferredHolder<Item, Item> COKE =
            ITEMS.register(
                    "coke",
                    () -> new Item(new Item.Properties().setId(COKE_ITEM_KEY))
            );

    // =========================
    // coke_block (BlockItem)
    // =========================
    private static final ResourceKey<Item> COKE_BLOCK_ITEM_KEY =
            ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "coke_block")
            );

    public static final DeferredHolder<Item, BlockItem> COKE_BLOCK_ITEM =
            ITEMS.register(
                    "coke_block",
                    () -> new BlockItem(
                            ModBlocks.COKE_BLOCK.get(),
                            new Item.Properties().setId(COKE_BLOCK_ITEM_KEY)
                    )
            );
    // =========================
    // COKE_OVEN_ITEM_KEY
    // =========================

    private static final ResourceKey<Item> COKE_OVEN_ITEM_KEY =
            ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "coke_oven")
            );
    public static final DeferredHolder<Item, BlockItem> COKE_OVEN_ITEM =
            ITEMS.register(
                    "coke_oven",
                    () -> new BlockItem(
                            ModBlocks.COKE_OVEN.get(),
                            new Item.Properties().setId(COKE_OVEN_ITEM_KEY)
                    )
            );



    private ModItems() {}
}
