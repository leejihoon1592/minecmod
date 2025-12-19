package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.block.FurnaceCoreBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    // ✅ 블록 레지스트리 (기초)
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MineBlacksmith.MODID);

    // =========================
    // furnace_core
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
    // coke_block
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

    private ModBlocks() {}
}
