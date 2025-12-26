package com.jangi10.mineblacksmith.blockentity;

import com.jangi10.mineblacksmith.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IngotMoldBlockEntity extends BlockEntity {

    private static final String N_METAL = "MetalId";
    private static final String N_FRAG  = "Fragments";

    private String metalIngotId = ""; // 결과 ingot id
    private int fragments = 0;        // 0~9

    public IngotMoldBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INGOT_MOLD_BE.get(), pos, state);
    }

    public String getMetalIngotId() { return metalIngotId; }
    public int getFragments() { return fragments; }

    public boolean canAccept(String ingotId) {
        return fragments == 0 || metalIngotId.isBlank() || metalIngotId.equals(ingotId);
    }

    public boolean addOne(String ingotId) {
        if (fragments >= 9) return false;
        if (!canAccept(ingotId)) return false;
        metalIngotId = ingotId;
        fragments++;
        setChanged();
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString(N_METAL, metalIngotId == null ? "" : metalIngotId);
        tag.putInt(N_FRAG, fragments);
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        super.loadAdditional(tag);
        metalIngotId = tag.getString(N_METAL);
        fragments = Math.max(0, tag.getInt(N_FRAG));
    }
}
