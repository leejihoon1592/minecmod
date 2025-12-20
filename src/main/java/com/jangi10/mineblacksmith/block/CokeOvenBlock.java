package com.jangi10.mineblacksmith.block;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.ModItems;
import com.jangi10.mineblacksmith.ModBlocks;
import com.jangi10.mineblacksmith.blockentity.CokeOvenBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class CokeOvenBlock extends AbstractFurnaceBlock {

    public static final MapCodec<CokeOvenBlock> CODEC = simpleCodec(CokeOvenBlock::new);

    public CokeOvenBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false));
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CokeOvenBlockEntity(pPos, pState);
    }

    // ✅ [최종 수정] 에러 없는 완전 수동 제어 로직
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!(pLevel instanceof ServerLevel)) return null;
        if (pBlockEntityType != ModBlockEntities.COKE_OVEN.get()) return null;

        return (level, pos, state, blockEntity) -> {
            if (blockEntity instanceof CokeOvenBlockEntity cokeOven) {

                // 1. 연료 소모 (리모컨 메서드 사용으로 에러 해결!)
                boolean isLit = cokeOven.getLitTime() > 0;
                boolean inventoryChanged = false;

                if (isLit) {
                    cokeOven.setLitTime(cokeOven.getLitTime() - 1);
                }

                // 2. 점화 로직 (불이 꺼졌을 때만)
                ItemStack inputStack = cokeOven.getItem(0);
                ItemStack fuelStack = cokeOven.getItem(1);

                if (!isLit && !inputStack.isEmpty() && !fuelStack.isEmpty()) {
                    int burnTime = 0;

                    // 연료별 지속 시간 설정 (9배율 적용)
                    if (fuelStack.is(Items.COAL)) burnTime = 1600;
                    else if (fuelStack.is(Items.COAL_BLOCK)) burnTime = 14400; // 1600 * 9
                    else if (fuelStack.is(ModItems.COKE.get())) burnTime = 3200;
                    else if (fuelStack.is(ModBlocks.COKE_BLOCK.get().asItem())) burnTime = 28800; // 3200 * 9

                    if (burnTime > 0) {
                        cokeOven.setLitTime(burnTime);
                        cokeOven.setLitDuration(burnTime);
                        fuelStack.shrink(1);
                        isLit = true;
                        inventoryChanged = true;
                    }
                }

                // 3. 굽기 로직 (수동 레시피)
                boolean canCook = false;
                if (isLit && !inputStack.isEmpty()) {
                    Item resultItem = null;
                    int cookTimeTotal = 2400;

                    // 재료에 따른 결과물 및 시간 설정
                    if (inputStack.is(Items.COAL)) {
                        resultItem = ModItems.COKE.get();
                        cookTimeTotal = 2400;  // 코크스 (2분)
                    } else if (inputStack.is(Items.COAL_BLOCK)) {
                        resultItem = ModBlocks.COKE_BLOCK.get().asItem();
                        cookTimeTotal = 21600; // 코크스 블록 (18분)
                    }

                    ItemStack outputStack = cokeOven.getItem(2);
                    if (resultItem != null) {
                        // 결과칸이 비었거나 같은 아이템일 때만 진행
                        if (outputStack.isEmpty() || (outputStack.is(resultItem) && outputStack.getCount() < outputStack.getMaxStackSize())) {
                            canCook = true;

                            int progress = cokeOven.getCookProgress();
                            cokeOven.setCookProgress(progress + 1);
                            cokeOven.setCookTotalTime(cookTimeTotal);

                            // 완료 시 처리
                            if (progress + 1 >= cookTimeTotal) {
                                inputStack.shrink(1); // 재료 소모
                                if (outputStack.isEmpty()) {
                                    cokeOven.setItem(2, new ItemStack(resultItem));
                                } else {
                                    outputStack.grow(1);
                                }
                                cokeOven.setCookProgress(0); // 초기화
                                inventoryChanged = true;
                            }
                        }
                    }
                }

                // 굽지 못하는 상태면 진행률 초기화
                if (!canCook) {
                    cokeOven.setCookProgress(0);
                }

                // 4. 상태 갱신 (시각 효과)
                if (state.getValue(LIT) != isLit) {
                    level.setBlock(pos, state.setValue(LIT, isLit), 3);
                }

                if (inventoryChanged) {
                    cokeOven.setChanged();
                }
            }
        };
    }

    @Override
    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof CokeOvenBlockEntity) {
            pPlayer.openMenu((MenuProvider)blockentity);
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            double d0 = (double)pPos.getX() + 0.5D;
            double d1 = (double)pPos.getY();
            double d2 = (double)pPos.getZ() + 0.5D;
            if (pRandom.nextDouble() < 0.1D) {
                pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            Direction direction = pState.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = pRandom.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
            double d6 = pRandom.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
            pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }
}