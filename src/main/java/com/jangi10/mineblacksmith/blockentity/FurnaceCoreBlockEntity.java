package com.jangi10.mineblacksmith.blockentity;

import com.jangi10.mineblacksmith.ModBlockEntities;
import com.jangi10.mineblacksmith.core.data.FurnaceSession;
import com.jangi10.mineblacksmith.core.data.IngotMoldState;
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

    private static final String S_ORE_ID  = "OreId";
    private static final String S_FUEL_ID = "FuelId";
    private static final String S_TEMP    = "Temperature";

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

        be.session.setFuelId(fuel.isEmpty() ? null : BuiltInRegistries.ITEM.getKey(fuel.getItem()).toString());
        be.session.setOreId(ore.isEmpty()  ? null : BuiltInRegistries.ITEM.getKey(ore.getItem()).toString());

        // ✅ 온도 갱신(가열/냉각)
        double cur = be.session.getTemperature();
        double next = cur;

        // fuelId -> FuelData 매칭(임시: ModFuels 상수들만)
        com.jangi10.mineblacksmith.core.data.FuelData fd = null;
        String fuelId = be.session.getFuelId();
        if (fuelId != null) {
            if (fuelId.equals(com.jangi10.mineblacksmith.core.init.ModFuels.WOOD.getTargetItemId())) fd = com.jangi10.mineblacksmith.core.init.ModFuels.WOOD;
            else if (fuelId.equals(com.jangi10.mineblacksmith.core.init.ModFuels.CHARCOAL.getTargetItemId())) fd = com.jangi10.mineblacksmith.core.init.ModFuels.CHARCOAL;
            else if (fuelId.equals(com.jangi10.mineblacksmith.core.init.ModFuels.COAL.getTargetItemId())) fd = com.jangi10.mineblacksmith.core.init.ModFuels.COAL;
            else if (fuelId.equals(com.jangi10.mineblacksmith.core.init.ModFuels.COKE.getTargetItemId())) fd = com.jangi10.mineblacksmith.core.init.ModFuels.COKE;
        }

        if (fd != null) {
            next = com.jangi10.mineblacksmith.core.logic.HeatPhysics.calculateNextTemperature(cur, fd, false);
        } else {
            // 연료 없으면 서서히 20도로 복귀(임시)
            next = Math.max(20.0, cur - 1.0);
        }

        be.session.setTemperature(next);

        // ✅ 연료 소모(임시): 5초(100tick)마다 1개씩
        if (!fuel.isEmpty() && level.getGameTime() % 1600 == 0) {
            fuel.shrink(1);
            if (fuel.isEmpty()) be.setStackInSlot(ModFurnaceSlots.SLOT_FUEL, ItemStack.EMPTY);
            else be.setStackInSlot(ModFurnaceSlots.SLOT_FUEL, fuel);
        }

        FurnaceLogic.tick(be.session);

        // ✅ 로그는 1초마다만
        if (level.getGameTime() % 20 == 0) {
            System.out.println("[MineBlacksmith] FurnaceCore tickServer at " + pos
                    + " temp=" + be.session.getTemperature()
                    + " fuel=" + be.session.getFuelId()
                    + " ore=" + be.session.getOreId());
            be.setChanged();
        }
    }



    // =========================
    // 슬롯 접근 (블록 우클릭 테스트용)
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
    // ✅ NeoForge 1.21.10 저장/로드 시그니처 (ValueIO)
    // =========================
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        // Session(객체 child)
        ValueOutput s = output.child(K_SESSION);
        if (session.getOreId() != null)  s.putString(S_ORE_ID, session.getOreId());
        if (session.getFuelId() != null) s.putString(S_FUEL_ID, session.getFuelId());
        s.putDouble(S_TEMP, session.getTemperature());

        // Items(객체 child)
        ValueOutput it = output.child(K_ITEMS);
        writeOneStack(it, I_FUEL_ID, I_FUEL_COUNT, fuelStack);
        writeOneStack(it, I_ORE_ID,  I_ORE_COUNT,  oreStack);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        // Session
        ValueInput s = input.childOrEmpty(K_SESSION);
        String oreId  = s.getStringOr(S_ORE_ID, "");
        String fuelId = s.getStringOr(S_FUEL_ID, "");
        double temp   = s.getDoubleOr(S_TEMP, 20.0);

        session.setOreId(oreId.isBlank() ? null : oreId);
        session.setFuelId(fuelId.isBlank() ? null : fuelId);
        session.setTemperature(temp);

        // Items
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

        // ✅ 반환 타입이 환경마다 갈려도 통과시키는 “안전 캐스팅”
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
