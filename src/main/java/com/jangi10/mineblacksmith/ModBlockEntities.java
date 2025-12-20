package com.jangi10.mineblacksmith;

import com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity;
import com.jangi10.mineblacksmith.blockentity.FurnaceCoreBlockEntity;
import com.jangi10.mineblacksmith.MineBlacksmith;
import com.jangi10.mineblacksmith.ModBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set; // ✅ 필수 Import

public class ModBlockEntities {

    // 레지스트리 생성
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MineBlacksmith.MODID);

    // 1. 기존 화로 코어 엔티티 (Builder 대신 new 사용)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FurnaceCoreBlockEntity>> FURNACE_CORE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("furnace_core_block_entity",
                    () -> new BlockEntityType<>(
                            FurnaceCoreBlockEntity::new,
                            Set.of(ModBlocks.FURNACE_CORE.get()) // ✅ Set.of() 사용
                    ));

    // 2. 코크스 오븐 엔티티 (Builder 대신 new 사용)
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("coke_oven_block_entity",
                    () -> new BlockEntityType<>(
                            CokeOvenBlockEntity::new,
                            Set.of(ModBlocks.COKE_OVEN.get()) // ✅ Set.of() 사용
                    ));

    // 레지스터 메서드
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}