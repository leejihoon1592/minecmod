package com.jangi10.mineblacksmith.block;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.ModItems;
import com.jangi10.mineblacksmith.blockentity.FurnaceCoreBlockEntity;
import com.jangi10.mineblacksmith.core.api.BlacksmithCoreAPI;
import com.jangi10.mineblacksmith.core.data.IngotExtractionResult;
import com.jangi10.mineblacksmith.core.init.ModFurnaceSlots;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FurnaceCoreBlock extends BaseEntityBlock {

    // ✅ 1.21.x 필수: codec() 구현용
    public static final MapCodec<FurnaceCoreBlock> CODEC = simpleCodec(FurnaceCoreBlock::new);

    public FurnaceCoreBlock(Properties props) {
        super(props);
    }

    /**
     * Research Codex로 우클릭하면 "주괴 추출"을 실행한다.
     * - 주괴틀/조각틀 UI가 아직 없으니, 지금은 파이프라인(광석→정제→주괴 생성)이 돌아가는지 확인하는 테스트용.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (!stack.is(ModItems.RESEARCH_CODEX.get())) {
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof FurnaceCoreBlockEntity furnace)) return InteractionResult.PASS;

        IngotExtractionResult result = BlacksmithCoreAPI.extractIngot(furnace.getSession());
        if (!result.isSuccess()) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("[주괴 추출 실패] " + result.getMessage()), true);
            return InteractionResult.CONSUME;
        }

        String outId = result.getResultIngotId();
        ResourceLocation rl = ResourceLocation.tryParse(outId);
        if (rl == null) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("[주괴 추출 실패] 잘못된 ID: " + outId), true);
            return InteractionResult.CONSUME;
        }

        // ✅ 환경별(1.21.x 레지스트리) 안전 조회
        Item item = resolveItemFromRegistry(rl);
        if (item == null || item == Items.AIR) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("[주괴 추출 실패] 아이템 미등록: " + outId), true);
            return InteractionResult.CONSUME;
        }

        // ✅ IngotExtractionResult에는 getResultCount()가 없음 → 테스트 단계에서는 1개 고정
        ItemStack out = new ItemStack(item, 1);
        if (!player.addItem(out)) {
            player.drop(out, false);
        }

        furnace.setChanged();
        player.displayClientMessage(net.minecraft.network.chat.Component.literal("[주괴 추출 성공] " + outId + " x" + out.getCount()), true);
        return InteractionResult.CONSUME;
    }

    /**
     * BuiltInRegistries.ITEM.getOptional() 이
     * - Optional<Item> 또는
     * - Optional<Holder.Reference<Item>>
     * 로 나오는 환경을 둘 다 처리한다.
     */
    @Nullable
    private static Item resolveItemFromRegistry(ResourceLocation id) {
        Optional<?> opt = BuiltInRegistries.ITEM.getOptional(id);
        if (opt.isEmpty()) return null;

        Object v = opt.get();

        // 1) Optional<Item>
        if (v instanceof Item item) return item;

        // 2) Optional<Holder.Reference<Item>>
        if (v instanceof Holder.Reference<?> ref) {
            Object value = ref.value();
            if (value instanceof Item item) return item;
        }

        return null;
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
                ? (lvl, p, st, be) -> FurnaceCoreBlockEntity.tickServer(lvl, p, st, (FurnaceCoreBlockEntity) be)
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
