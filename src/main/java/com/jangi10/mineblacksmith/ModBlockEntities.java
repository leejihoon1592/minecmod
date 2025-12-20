package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.blockentity.FurnaceCoreBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MineBlacksmith.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FurnaceCoreBlockEntity>> FURNACE_CORE_BE =
            BLOCK_ENTITY_TYPES.register("furnace_core",
                    () -> new BlockEntityType<>(
                            FurnaceCoreBlockEntity::new,
                            false,
                            ModBlocks.FURNACE_CORE.get()
                    ));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity>> COKE_OVEN =
            BLOCK_ENTITY_TYPES.register("coke_oven",
                    () -> new BlockEntityType<>(
                            com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity::new,
                            false,
                            ModBlocks.COKE_OVEN.get()
                    ));




    private ModBlockEntities() {}
}
