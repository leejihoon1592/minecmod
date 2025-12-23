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

    // ✅ 가이드북(연구 도감) - 아직은 아이템만 등록, UI는 이후 연결
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
    // Material Items (raw / ingot / nugget)
    // ------------------------------------------------------------

    /**
     * 금속 ID 목록(텍스처/데이터 기준)
     * - textures/item/ingot/<id>_ingot.png
     * - textures/item/nugget/<id>_nugget.png
     * - textures/item/raw/raw_<id>.png
     */
    public static final List<String> METAL_IDS = List.of(
            "accutyrone", "biklis", "brackstal", "charvin", "clinsor",
            "conductite", "criminaltalloi", "duracilium", "durathorn", "edgastium",
            "enduri", "flexarite", "ginspa", "granteron", "liselborn",
            "lumesor", "sprinal", "stevalite", "tenaloct", "voltarite"
    );

    // 금속별 아이템 홀더 모음 (필요하면 다른 곳에서 반복 없이 참조 가능)
    public static final Map<String, DeferredHolder<Item, Item>> INGOTS = new LinkedHashMap<>();
    public static final Map<String, DeferredHolder<Item, Item>> NUGGETS = new LinkedHashMap<>();
    public static final Map<String, DeferredHolder<Item, Item>> RAWS = new LinkedHashMap<>();

    static {
        for (String id : METAL_IDS) {
            INGOTS.put(id, registerSimple(id + "_ingot"));
            NUGGETS.put(id, registerSimple(id + "_nugget"));
            RAWS.put(id, registerSimple("raw_" + id));
        }
    }

    private ModItems() {}
}
