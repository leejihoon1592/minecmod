package com.jangi10.mineblacksmith.block;

import com.jangi10.mineblacksmith.ModItems;
import com.jangi10.mineblacksmith.blockentity.IngotMoldBlockEntity;
import com.jangi10.mineblacksmith.item.IncompleteIngotItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IngotMoldBlock extends BaseEntityBlock {

    public static final MapCodec<IngotMoldBlock> CODEC = simpleCodec(IngotMoldBlock::new);

    public IngotMoldBlock(Properties props) { super(props); }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IngotMoldBlockEntity(pos, state);
    }

    /**
     * ✅ 뼈대 단계 안전 동작:
     * - 플레이어가 맨손 우클릭하면 "틀 회수"
     * - 내부 조각/주괴를 드랍하고 블록을 제거
     *
     * (onRemove 오버라이드는 네 매핑에서 시그니처가 달라 컴파일을 막으므로 사용하지 않음)
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof IngotMoldBlockEntity mold)) return InteractionResult.PASS;

        int frag = mold.getFragments();
        String metal = mold.getMetalIngotId();

        if (frag > 0 && metal != null && !metal.isBlank()) {
            ItemStack drop;
            if (frag >= 9) {
                Item ingotItem = resolveItem(metal);
                drop = (ingotItem == null || ingotItem == Items.AIR)
                        ? ItemStack.EMPTY
                        : new ItemStack(ingotItem, 1);
            } else {
                drop = IncompleteIngotItem.make(ModItems.INCOMPLETE_INGOT.get(), metal, frag, 9);
            }

            if (!drop.isEmpty()) {
                popResource(level, pos, drop);
            }
        }

        // 블록 제거(회수)
        level.removeBlock(pos, false);

        // 회수 성공
        player.swing(InteractionHand.MAIN_HAND);
        return InteractionResult.CONSUME;
    }

    @Nullable
    private static Item resolveItem(String id) {
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) return null;

        // ✅ 너 환경: Optional<Item>
        Optional<Item> opt = BuiltInRegistries.ITEM.getOptional(rl);
        return opt.orElse(null);
    }
}
