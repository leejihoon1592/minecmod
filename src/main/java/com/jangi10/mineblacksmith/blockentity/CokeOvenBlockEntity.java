package com.jangi10.mineblacksmith.blockentity;

import com.jangi10.mineblacksmith.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Coke Oven BE (구현 단계)
 * - FurnaceMenu가 이 BE(Container)를 직접 보게 연결
 * - 저장/로드는 NeoForge 1.21.10 방식(ValueIO)로 처리
 * - stillValid로 "블록 파괴 시 UI 즉시 닫힘" 보장
 *
 * 슬롯(바닐라 화로 동일):
 * 0 = 입력, 1 = 연료, 2 = 출력
 */
public class CokeOvenBlockEntity extends BlockEntity implements MenuProvider, Container {

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    // 진행바용(지금 단계는 자리만)
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime = 2400;

    // ===== ValueIO 키 =====
    private static final String K_ITEMS = "Items";
    private static final String K_STATE = "State";

    private static final String I0_ID = "I0_Id";
    private static final String I0_CT = "I0_Ct";
    private static final String I1_ID = "I1_Id";
    private static final String I1_CT = "I1_Ct";
    private static final String I2_ID = "I2_Id";
    private static final String I2_CT = "I2_Ct";

    private static final String S_LIT = "LitTime";
    private static final String S_LIT_TOTAL = "LitDuration";
    private static final String S_COOK = "CookProgress";
    private static final String S_COOK_TOTAL = "CookTotal";

    private final ContainerData data = new ContainerData() {
        @Override public int get(int i) {
            return switch (i) {
                case 0 -> litTime;
                case 1 -> litDuration;
                case 2 -> cookingProgress;
                case 3 -> cookingTotalTime;
                default -> 0;
            };
        }
        @Override public void set(int i, int v) {
            switch (i) {
                case 0 -> litTime = v;
                case 1 -> litDuration = v;
                case 2 -> cookingProgress = v;
                case 3 -> cookingTotalTime = v;
            }
        }
        @Override public int getCount() { return 4; }
    };

    public CokeOvenBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COKE_OVEN.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Coke Oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new com.jangi10.mineblacksmith.menu.CokeOvenMenu(id, inv, this, data);
    }

    // ===== Container 구현 =====
    @Override public int getContainerSize() { return 3; }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : items) if (!s.isEmpty()) return false;
        return true;
    }

    @Override public ItemStack getItem(int slot) { return items.get(slot); }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = items.get(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack split = stack.split(amount);
        if (!split.isEmpty()) setChanged();
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack s = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
    }

    /**
     * ✅ 블록 파괴 순간 UI 닫힘(복사버그 차단 핵심)
     */
    @Override
    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    // ===== 저장/로드(ValueIO) =====
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput it = output.child(K_ITEMS);
        writeOneStack(it, I0_ID, I0_CT, items.get(0));
        writeOneStack(it, I1_ID, I1_CT, items.get(1));
        writeOneStack(it, I2_ID, I2_CT, items.get(2));

        ValueOutput st = output.child(K_STATE);
        st.putInt(S_LIT, litTime);
        st.putInt(S_LIT_TOTAL, litDuration);
        st.putInt(S_COOK, cookingProgress);
        st.putInt(S_COOK_TOTAL, cookingTotalTime);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        ValueInput it = input.childOrEmpty(K_ITEMS);
        items = NonNullList.withSize(3, ItemStack.EMPTY);
        items.set(0, readOneStack(it, I0_ID, I0_CT));
        items.set(1, readOneStack(it, I1_ID, I1_CT));
        items.set(2, readOneStack(it, I2_ID, I2_CT));

        ValueInput st = input.childOrEmpty(K_STATE);
        litTime = st.getIntOr(S_LIT, 0);
        litDuration = st.getIntOr(S_LIT_TOTAL, 0);
        cookingProgress = st.getIntOr(S_COOK, 0);
        cookingTotalTime = st.getIntOr(S_COOK_TOTAL, 2400);
    }

    private static void writeOneStack(ValueOutput out, String keyId, String keyCount, ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            out.putString(keyId, "");
            out.putInt(keyCount, 0);
            return;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        out.putString(keyId, id.toString());
        out.putInt(keyCount, stack.getCount());
    }

    private static ItemStack readOneStack(ValueInput in, String keyId, String keyCount) {
        String idStr = in.getStringOr(keyId, "");
        int count = in.getIntOr(keyCount, 0);
        if (idStr.isBlank() || count <= 0) return ItemStack.EMPTY;

        ResourceLocation id = ResourceLocation.tryParse(idStr);
        if (id == null) return ItemStack.EMPTY;

        Optional<?> opt = BuiltInRegistries.ITEM.getOptional(id);
        if (opt.isEmpty()) return ItemStack.EMPTY;

        Object v = opt.get();
        if (v instanceof Item item) return new ItemStack(item, count);

        if (v instanceof Holder.Reference<?> ref) {
            Object value = ref.value();
            if (value instanceof Item item) return new ItemStack(item, count);
        }
        return ItemStack.EMPTY;
    }

    // (테스트 편의)
    public NonNullList<ItemStack> getItems() { return items; }
}
