package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.blockentity.FurnaceCoreBlockEntity;
import com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MineBlacksmith.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FurnaceCoreBlockEntity>> FURNACE_CORE_BE =
            BLOCK_ENTITY_TYPES.register("furnace_core",
                    () -> new BlockEntityType<>(
                            FurnaceCoreBlockEntity::new,
                            Set.of(ModBlocks.FURNACE_CORE.get())
                    ));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN =
            BLOCK_ENTITY_TYPES.register("coke_oven",
                    () -> new BlockEntityType<>(
                            CokeOvenBlockEntity::new,
                            Set.of(ModBlocks.COKE_OVEN.get())
                    ));

    private ModBlockEntities() {}
}