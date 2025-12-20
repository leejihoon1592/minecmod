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
        // [설정] 화로 타입 정의
        super(ModBlockEntities.COKE_OVEN.get(), pPos, pBlockState, RecipeType.SMELTING);
    }

    // ==========================================
    // ✅ [핵심] 외부(Block)에서 사용할 리모컨 메서드들
    // ==========================================

    // 1. 불타는 시간 (남은 시간)
    public int getLitTime() {
        return this.dataAccess.get(0);
    }
    public void setLitTime(int time) {
        this.dataAccess.set(0, time);
    }

    // 2. 전체 불타는 시간 (화살표 표시용)
    public void setLitDuration(int time) {
        this.dataAccess.set(1, time);
    }

    // 3. 굽기 진행률 (화살표가 얼마나 찼는지)
    public int getCookProgress() {
        return this.dataAccess.get(2);
    }
    public void setCookProgress(int progress) {
        this.dataAccess.set(2, progress);
    }

    // 4. 총 굽는 시간 (화살표가 꽉 차는데 걸리는 시간)
    public void setCookTotalTime(int time) {
        this.dataAccess.set(3, time);
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