package com.jangi10.mineblacksmith.blockentity;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.core.data.FurnaceSession;
import com.jangi10.mineblacksmith.core.data.IngotMoldState;
import com.jangi10.mineblacksmith.core.init.ModFuels;
import com.jangi10.mineblacksmith.core.init.ModFurnaceSlots;
import com.jangi10.mineblacksmith.core.logic.FurnaceLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

public class FurnaceCoreBlockEntity extends BlockEntity {

    private final FurnaceSession session = new FurnaceSession(new IngotMoldState());

    // ✅ 임시 슬롯 2칸 (연료/광물)
    private ItemStack fuelStack = ItemStack.EMPTY;
    private ItemStack oreStack  = ItemStack.EMPTY;

    // ValueIO 키
    private static final String K_SESSION = "Session";
    private static final String K_ITEMS   = "Items";

    private static final String S_ORE_ID      = "OreId";
    private static final String S_FUEL_ID     = "FuelId";
    private static final String S_TEMP        = "Temperature";
    private static final String S_BURN_TICKS  = "BurnTicks";
    private static final String S_BURN_TOTAL  = "BurnTicksTotal";

    private static final String I_FUEL_ID    = "FuelItemId";
    private static final String I_FUEL_COUNT = "FuelCount";
    private static final String I_ORE_ID     = "OreItemId";
    private static final String I_ORE_COUNT  = "OreCount";

    public FurnaceCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_CORE_BE.get(), pos, state);
    }

    // =========================
    // 서버 틱
    // =========================
    public static void tickServer(Level level, BlockPos pos, BlockState state, FurnaceCoreBlockEntity be) {
        if (level.isClientSide()) return;

        ItemStack fuel = be.getStackInSlot(ModFurnaceSlots.SLOT_FUEL);
        ItemStack ore  = be.getStackInSlot(ModFurnaceSlots.SLOT_ORE);

        // oreId는 그냥 슬롯 반영(언제나 OK)
        be.session.setOreId(ore.isEmpty() ? null : BuiltInRegistries.ITEM.getKey(ore.getItem()).toString());

        // ==========================================
        // ✅ 바닐라 화로 방식 "연소 상태 머신"
        // - burnTicks > 0 : 불 켜짐(연료 추가 투입해도 소모 없음)
        // - burnTicks == 0 : 그 순간에만 연료 1개 소모(-1) + burnTicksTotal 세팅
        // ==========================================
        int burn = be.session.getBurnTicks();
        int burnTotal = be.session.getBurnTicksTotal();

        // 1) 불이 켜져 있으면 burnTicks 감소
        if (burn > 0) {
            burn--;
            be.session.setBurnTicks(burn);
        }

        // 2) 불이 꺼진 상태(burn==0)라면, 연료가 있을 때 "점화" (여기서만 -1)
        if (burn == 0) {
            // 다음 점화를 위한 fuelId는 슬롯 기준으로 갱신
            String slotFuelId = fuel.isEmpty() ? null : BuiltInRegistries.ITEM.getKey(fuel.getItem()).toString();
            be.session.setFuelId(slotFuelId);

            if (!fuel.isEmpty() && slotFuelId != null) {
                int nextBurnTotal = ModFuels.getBurnTicksForItemId(slotFuelId); // 예: 석탄 1600
                if (nextBurnTotal > 0) {
                    // ✅ 점화 순간에만 연료 1개 소모
                    fuel.shrink(1);
                    be.setStackInSlot(ModFurnaceSlots.SLOT_FUEL, fuel.isEmpty() ? ItemStack.EMPTY : fuel);

                    be.session.setBurnTicksTotal(nextBurnTotal);
                    be.session.setBurnTicks(nextBurnTotal);

                    burn = nextBurnTotal;
                    burnTotal = nextBurnTotal;
                    // fuelId는 "이번에 태우는 연료"로 확정된 값(=slotFuelId)
                    be.session.setFuelId(slotFuelId);
                }
            }
        } else {
            // 불이 켜져 있는 동안에는 fuelId를 "현재 연소 중인 연료"로 유지
            // (슬롯에 추가로 넣어도 연소 연료가 바뀌지 않게)
            if (be.session.getFuelId() == null && !fuel.isEmpty()) {
                be.session.setFuelId(BuiltInRegistries.ITEM.getKey(fuel.getItem()).toString());
            }
        }

        boolean isBurning = be.session.getBurnTicks() > 0;

        // ==========================================
        // ✅ 온도 시스템(우리 것) — 연소 상태에 따라 가열/냉각
        // ==========================================
        double cur = be.session.getTemperature();
        double next = cur;

        com.jangi10.mineblacksmith.core.data.FuelData fd = null;
        String burningFuelId = be.session.getFuelId();

        if (burningFuelId != null) {
            if (burningFuelId.equals(ModFuels.WOOD.getTargetItemId())) fd = ModFuels.WOOD;
            else if (burningFuelId.equals(ModFuels.CHARCOAL.getTargetItemId())) fd = ModFuels.CHARCOAL;
            else if (burningFuelId.equals(ModFuels.COAL.getTargetItemId())) fd = ModFuels.COAL;
            else if (burningFuelId.equals(ModFuels.COKE.getTargetItemId())) fd = ModFuels.COKE;
        }

        if (isBurning && fd != null) {
            next = com.jangi10.mineblacksmith.core.logic.HeatPhysics.calculateNextTemperature(cur, fd, false);
        } else {
            // 불 꺼짐(또는 연료데이터 없음) => 냉각
            next = Math.max(20.0, cur - 1.0);
        }

        be.session.setTemperature(next);

        // 코어 로직 틱
        FurnaceLogic.tick(be.session);

        // 1초마다만 dirty
        if (level.getGameTime() % 20 == 0) {
            be.setChanged();
        }
    }

    // =========================
    // 슬롯 접근 (임시)
    // =========================
    public ItemStack getStackInSlot(int slot) {
        if (slot == ModFurnaceSlots.SLOT_FUEL) return fuelStack.copy();
        if (slot == ModFurnaceSlots.SLOT_ORE)  return oreStack.copy();
        return ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack safe = (stack == null) ? ItemStack.EMPTY : stack.copy();

        if (slot == ModFurnaceSlots.SLOT_FUEL) fuelStack = safe;
        else if (slot == ModFurnaceSlots.SLOT_ORE) oreStack = safe;
        else return;

        setChanged();
    }

    public FurnaceSession getSession() {
        return session;
    }

    // =========================
    // ✅ NeoForge 1.21.10 저장/로드 (ValueIO)
    // =========================
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput s = output.child(K_SESSION);

        if (session.getOreId() != null)  s.putString(S_ORE_ID, session.getOreId());
        if (session.getFuelId() != null) s.putString(S_FUEL_ID, session.getFuelId());

        s.putDouble(S_TEMP, session.getTemperature());
        s.putInt(S_BURN_TICKS, session.getBurnTicks());
        s.putInt(S_BURN_TOTAL, session.getBurnTicksTotal());

        ValueOutput it = output.child(K_ITEMS);
        writeOneStack(it, I_FUEL_ID, I_FUEL_COUNT, fuelStack);
        writeOneStack(it, I_ORE_ID,  I_ORE_COUNT,  oreStack);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        ValueInput s = input.childOrEmpty(K_SESSION);

        String oreId  = s.getStringOr(S_ORE_ID, "");
        String fuelId = s.getStringOr(S_FUEL_ID, "");
        double temp   = s.getDoubleOr(S_TEMP, 20.0);

        int burnTicks = s.getIntOr(S_BURN_TICKS, 0);
        int burnTotal = s.getIntOr(S_BURN_TOTAL, 0);

        session.setOreId(oreId.isBlank() ? null : oreId);
        session.setFuelId(fuelId.isBlank() ? null : fuelId);
        session.setTemperature(temp);

        session.setBurnTicks(burnTicks);
        session.setBurnTicksTotal(burnTotal);

        ValueInput it = input.childOrEmpty(K_ITEMS);
        fuelStack = readOneStack(it, I_FUEL_ID, I_FUEL_COUNT);
        oreStack  = readOneStack(it, I_ORE_ID,  I_ORE_COUNT);
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

        // 1) Optional<Item> 인 환경
        if (v instanceof Item item) {
            return new ItemStack(item, count);
        }

        // 2) Optional<Holder.Reference<Item>> 인 환경
        if (v instanceof Holder.Reference<?> ref) {
            Object value = ref.value();
            if (value instanceof Item item) {
                return new ItemStack(item, count);
            }
        }

        return ItemStack.EMPTY;
    }
}
