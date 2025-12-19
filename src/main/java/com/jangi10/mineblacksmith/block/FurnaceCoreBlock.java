package com.jangi10.mineblacksmith.block;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.blockentity.FurnaceCoreBlockEntity;
import com.jangi10.mineblacksmith.core.init.ModFurnaceSlots;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FurnaceCoreBlock extends BaseEntityBlock {

    // ✅ 1.21.x 필수: codec() 구현용
    public static final MapCodec<FurnaceCoreBlock> CODEC = simpleCodec(FurnaceCoreBlock::new);

    public FurnaceCoreBlock(Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FurnaceCoreBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return type == ModBlockEntities.FURNACE_CORE_BE.get()
                ? (lvl, pos, st, be) -> FurnaceCoreBlockEntity.tickServer(lvl, pos, st, (FurnaceCoreBlockEntity) be)
                : null;
    }

    // ✅ 임시 우클릭 입력:
    // - 기본: 연료 슬롯
    // - 쉬프트: 광물 슬롯
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof FurnaceCoreBlockEntity furnace)) return InteractionResult.PASS;

        boolean oreMode = player.isShiftKeyDown();
        int slot = oreMode ? ModFurnaceSlots.SLOT_ORE : ModFurnaceSlots.SLOT_FUEL;

        ItemStack inSlot = furnace.getStackInSlot(slot);

        // 손 비었으면 -> 슬롯에서 꺼내서 손에 줌
        if (player.getMainHandItem().isEmpty()) {
            if (!inSlot.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, inSlot.copy());
                furnace.setStackInSlot(slot, ItemStack.EMPTY);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.CONSUME;
        }

        // 손에 아이템 있으면 -> 슬롯에 "가능한 만큼" 넣기(누적)
        ItemStack hand = player.getMainHandItem();

// 슬롯 최대(보통 64)
        int max = hand.getMaxStackSize();
        int cur = inSlot.isEmpty() ? 0 : inSlot.getCount();

// 슬롯에 같은 아이템만 누적 허용
        if (inSlot.isEmpty() || ItemStack.isSameItemSameComponents(inSlot, hand)) {

            int space = Math.max(0, max - cur);
            int move = Math.min(space, hand.getCount());

            if (move > 0) {
                if (inSlot.isEmpty()) {
                    ItemStack toInsert = hand.copy();
                    toInsert.setCount(move);
                    furnace.setStackInSlot(slot, toInsert);
                } else {
                    inSlot.grow(move);
                    furnace.setStackInSlot(slot, inSlot);
                }

                hand.shrink(move);
                player.setItemInHand(InteractionHand.MAIN_HAND, hand);

                furnace.setChanged();
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.CONSUME;

    }
}

