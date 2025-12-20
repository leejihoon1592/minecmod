package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.blockentity.FurnaceCoreBlockEntity;
import com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set; // ✅ 필수 Import (Set.of 사용을 위해)

public class ModBlockEntities {

    // 레지스트리 생성
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MineBlacksmith.MODID);

    // ✅ FURNACE_CORE_BE (FurnaceCoreBlock에서 참조하는 이름과 통일)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FurnaceCoreBlockEntity>> FURNACE_CORE_BE =
            BLOCK_ENTITY_TYPES.register("furnace_core",
                    () -> new BlockEntityType<>(
                            FurnaceCoreBlockEntity::new,
                            Set.of(ModBlocks.FURNACE_CORE.get()) // 1.21 표준: Set.of() 사용, 'false' 제거
                    ));

    // ✅ COKE_OVEN (기존 이름 유지)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN =
            BLOCK_ENTITY_TYPES.register("coke_oven",
                    () -> new BlockEntityType<>(
                            CokeOvenBlockEntity::new,
                            Set.of(ModBlocks.COKE_OVEN.get()) // 1.21 표준
                    ));

    private ModBlockEntities() {}
}