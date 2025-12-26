package com.jangi10.mineblacksmith.blockentity;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.block.FurnaceOutputPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceOutputPipeBlockEntity extends BlockEntity {

    public FurnaceOutputPipeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_OUTPUT_PIPE_BE.get(), pos, state);
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, FurnaceOutputPipeBlockEntity be) {
        boolean locked = state.getValue(FurnaceOutputPipeBlock.LOCKED);
        if (locked) return;

        // 1) 옆(4방향)에서 FurnaceCore 찾기
        FurnaceCoreBlockEntity furnace = null;
        for (Direction d : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
            BlockEntity other = level.getBlockEntity(pos.relative(d));
            if (other instanceof FurnaceCoreBlockEntity f) {
                furnace = f;
                break;
            }
        }
        if (furnace == null) return;

        // 2) 아래에서 주괴틀 찾기
        BlockEntity below = level.getBlockEntity(pos.below());
        if (!(below instanceof IngotMoldBlockEntity mold)) {
            // 주괴틀이 없으면 “증발 손실”(1조각씩 날림)
            if (furnace.getSession().getMeltFragments() > 0) {
                furnace.getSession().drainOneFragment();
                furnace.setChanged();
            }
            return;
        }

        // 3) 용탕이 있으면 1조각 흘려보내기
        String metal = furnace.getSession().getMeltIngotId();
        if (metal == null || metal.isBlank()) return;
        if (furnace.getSession().getMeltFragments() <= 0) return;

        if (!mold.canAccept(metal)) {
            // 다른 금속 섞임은 합금 단계에서 처리. 지금은 손실 처리(한 조각 증발)
            furnace.getSession().drainOneFragment();
            furnace.setChanged();
            return;
        }

        if (mold.addOne(metal)) {
            furnace.getSession().drainOneFragment();
            furnace.setChanged();
        }
    }
}
