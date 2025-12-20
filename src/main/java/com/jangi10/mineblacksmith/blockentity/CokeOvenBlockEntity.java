package com.jangi10.mineblacksmith.blockentity;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.menu.CokeOvenMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CokeOvenBlockEntity extends AbstractFurnaceBlockEntity {

    public CokeOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        // [수정 완료] COKE_OVEN_BLOCK_ENTITY -> COKE_OVEN 으로 변경
        super(ModBlockEntities.COKE_OVEN.get(), pPos, pBlockState, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.mineblacksmith.coke_oven");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new CokeOvenMenu(pContainerId, pInventory, this, this.dataAccess);
    }
}