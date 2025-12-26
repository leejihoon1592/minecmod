package com.jangi10.mineblacksmith.block;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.blockentity.FurnaceOutputPipeBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FurnaceOutputPipeBlock extends BaseEntityBlock {

    public static final MapCodec<FurnaceOutputPipeBlock> CODEC = simpleCodec(FurnaceOutputPipeBlock::new);
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    public FurnaceOutputPipeBlock(Properties props) {
        super(props);
        registerDefaultState(this.stateDefinition.any().setValue(LOCKED, false)); // 기본 OPEN
    }

    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FurnaceOutputPipeBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return type == ModBlockEntities.FURNACE_OUTPUT_PIPE_BE.get()
                ? (lvl, p, st, be) -> FurnaceOutputPipeBlockEntity.tickServer(lvl, p, st, (FurnaceOutputPipeBlockEntity) be)
                : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        boolean locked = state.getValue(LOCKED);
        level.setBlock(pos, state.setValue(LOCKED, !locked), 3);
        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(LOCKED);
    }
}
