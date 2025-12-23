package com.jangi10.mineblacksmith;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModItems {

    // ✅ 아이템 레지스트리
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MineBlacksmith.MODID);

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------
    private static ResourceKey<Item> key(String id) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, id));
    }

    private static DeferredHolder<Item, Item> registerSimple(String id) {
        ResourceKey<Item> k = key(id);
        return ITEMS.register(id, () -> new Item(new Item.Properties().setId(k)));
    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String id, DeferredHolder<Block, ? extends Block> block) {
        ResourceKey<Item> k = key(id);
        return ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties().setId(k)));
    }

    // ------------------------------------------------------------
    // Function Items
    // ------------------------------------------------------------

    // ✅ 가이드북(연구 도감)
    public static final DeferredHolder<Item, Item> RESEARCH_CODEX = registerSimple("research_codex");

    // ------------------------------------------------------------
    // Existing items / blockitems
    // ------------------------------------------------------------

    public static final DeferredHolder<Item, BlockItem> FURNACE_CORE_ITEM =
            registerBlockItem("furnace_core", ModBlocks.FURNACE_CORE);

    public static final DeferredHolder<Item, Item> COKE =
            registerSimple("coke");

    public static final DeferredHolder<Item, BlockItem> COKE_BLOCK_ITEM =
            registerBlockItem("coke_block", ModBlocks.COKE_BLOCK);

    public static final DeferredHolder<Item, BlockItem> COKE_OVEN_ITEM =
            registerBlockItem("coke_oven", ModBlocks.COKE_OVEN);

    // ------------------------------------------------------------
    // Material IDs
    // ------------------------------------------------------------
    public static final List<String> METAL_IDS = ModMetalIds.METAL_IDS;

    // ------------------------------------------------------------
    // Material Items (raw / ingot / nugget)
    // ------------------------------------------------------------
    public static final Map<String, DeferredHolder<Item, Item>> INGOTS = new LinkedHashMap<>();
    public static final Map<String, DeferredHolder<Item, Item>> NUGGETS = new LinkedHashMap<>();
    public static final Map<String, DeferredHolder<Item, Item>> RAWS = new LinkedHashMap<>();

    // ------------------------------------------------------------
    // Material BlockItems (stone ore / raw block)
    // - 블록이 ModBlocks에 등록되어 있으므로 여기서 BlockItem만 등록하면 됨
    // ------------------------------------------------------------
    public static final Map<String, DeferredHolder<Item, BlockItem>> STONE_ORE_BLOCK_ITEMS = new LinkedHashMap<>();
    public static final Map<String, DeferredHolder<Item, BlockItem>> RAW_BLOCK_BLOCK_ITEMS = new LinkedHashMap<>();

    static {
        for (String id : METAL_IDS) {
            // 아이템
            INGOTS.put(id, registerSimple(id + "_ingot"));
            NUGGETS.put(id, registerSimple(id + "_nugget"));
            RAWS.put(id, registerSimple("raw_" + id));

            // 블록 아이템
            STONE_ORE_BLOCK_ITEMS.put(id, registerBlockItem(id + "_ore", ModBlocks.STONE_ORES.get(id)));
            RAW_BLOCK_BLOCK_ITEMS.put(id, registerBlockItem("raw_" + id + "_block", ModBlocks.RAW_BLOCKS.get(id)));
        }
    }

    private ModItems() {}
}
