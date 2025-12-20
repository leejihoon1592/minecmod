package com.jangi10.mineblacksmith.block;

import com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Coke Oven Block
 * - BaseEntityBlock 기반 (1.21.x)
 * - 우클릭: UI 열기
 * - 파괴 시 드랍은 BlockEntity.setRemoved()에서 처리 (onRemove 훅 안 씀)
 */
public class CokeOvenBlock extends BaseEntityBlock {

    public static final MapCodec<CokeOvenBlock> CODEC = simpleCodec(CokeOvenBlock::new);

    public CokeOvenBlock(Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // ✅ 네 프로젝트 스타일과 동일: protected 로 오버라이드해야 함 (FurnaceCoreBlock 참고)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MenuProvider provider) {
            player.openMenu(provider);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CokeOvenBlockEntity(pos, state);
    }
}

