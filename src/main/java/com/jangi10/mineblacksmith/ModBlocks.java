package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.block.CokeOvenBlock;
import com.jangi10.mineblacksmith.block.FurnaceCoreBlock;
import com.jangi10.mineblacksmith.block.FurnaceOutputPipeBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModBlocks {

    // ✅ 블록 레지스트리
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MineBlacksmith.MODID);

    // =========================
    // 0. Furnace Output Pipe (출력 파이프)
    // =========================
    private static final ResourceKey<Block> FURNACE_OUTPUT_PIPE_KEY =
            ResourceKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "furnace_output_pipe")
            );

    public static final DeferredHolder<Block, FurnaceOutputPipeBlock> FURNACE_OUTPUT_PIPE =
            BLOCKS.register(
                    "furnace_output_pipe",
                    () -> new FurnaceOutputPipeBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.METAL)
                                    .strength(2.0f)
                                    .setId(FURNACE_OUTPUT_PIPE_KEY)
                    )
            );

    // =========================
    // 1. Furnace Core (화로 코어)
    // =========================
    private static final ResourceKey<Block> FURNACE_CORE_KEY =
            ResourceKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "furnace_core")
            );

    public static final DeferredHolder<Block, FurnaceCoreBlock> FURNACE_CORE =
            BLOCKS.register(
                    "furnace_core",
                    () -> new FurnaceCoreBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.METAL)
                                    .strength(3.0f)
                                    .setId(FURNACE_CORE_KEY)
                    )
            );

    // =========================
    // 2. Coke Block (코크스 블록 - 연료 뭉치)
    // =========================
    private static final ResourceKey<Block> COKE_BLOCK_KEY =
            ResourceKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "coke_block")
            );

    public static final DeferredHolder<Block, Block> COKE_BLOCK =
            BLOCKS.register(
                    "coke_block",
                    () -> new Block(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_BLACK)
                                    .strength(4.0f)
                                    .setId(COKE_BLOCK_KEY)
                    )
            );

    // =========================
    // 3. Coke Oven (코크스 오븐 - 기계)
    // =========================
    private static final ResourceKey<Block> COKE_OVEN_KEY =
            ResourceKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, "coke_oven")
            );

    public static final DeferredHolder<Block, CokeOvenBlock> COKE_OVEN =
            BLOCKS.register(
                    "coke_oven",
                    () -> new CokeOvenBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.STONE)
                                    .strength(3.5F)
                                    .requiresCorrectToolForDrops()
                                    .setId(COKE_OVEN_KEY)
                    )
            );

    // ============================================================
    // Stone Ores (stone base) : <metal>_ore
    // ============================================================
    public static final Map<String, DeferredHolder<Block, Block>> STONE_ORES = new LinkedHashMap<>();

    // Raw Blocks : raw_<metal>_block (원석 블록)
    public static final Map<String, DeferredHolder<Block, Block>> RAW_BLOCKS = new LinkedHashMap<>();

    static {
        for (String id : ModMetalIds.METAL_IDS) {

            // stone ore
            {
                String blockId = id + "_ore";
                ResourceKey<Block> key = ResourceKey.create(
                        Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, blockId)
                );

                STONE_ORES.put(id, BLOCKS.register(blockId, () ->
                        new Block(BlockBehaviour.Properties.of()
                                .mapColor(MapColor.STONE)
                                .strength(3.0f, 3.0f)
                                .requiresCorrectToolForDrops()
                                .setId(key)
                        )
                ));
            }

            // raw block
            {
                String blockId = "raw_" + id + "_block";
                ResourceKey<Block> key = ResourceKey.create(
                        Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(MineBlacksmith.MODID, blockId)
                );

                RAW_BLOCKS.put(id, BLOCKS.register(blockId, () ->
                        new Block(BlockBehaviour.Properties.of()
                                .mapColor(MapColor.STONE)
                                .strength(5.0f, 6.0f)
                                .requiresCorrectToolForDrops()
                                .setId(key)
                        )
                ));
            }
        }
    }

    private ModBlocks() {}
}
