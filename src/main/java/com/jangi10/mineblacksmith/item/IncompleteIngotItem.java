package com.jangi10.mineblacksmith.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class IncompleteIngotItem extends Item {

    public static final String N_METAL = "MetalId";      // 예: mineblacksmith:iron_ingot
    public static final String N_CUR   = "Fragments";    // 1~8
    public static final String N_MAX   = "MaxFragments"; // 9

    public IncompleteIngotItem(Properties props) {
        super(props);
    }

    // -----------------------------
    // CustomData(DataComponents.CUSTOM_DATA) 헬퍼
    // -----------------------------
    private static CompoundTag readTag(ItemStack stack) {
        CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
        return cd == null ? new CompoundTag() : cd.copyTag();
    }

    private static void writeTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // -----------------------------
    // 생성/조회
    // -----------------------------
    public static ItemStack make(Item item, String metalId, int fragments, int max) {
        ItemStack stack = new ItemStack(item, 1);
        CompoundTag tag = readTag(stack);
        tag.putString(N_METAL, metalId == null ? "" : metalId);
        tag.putInt(N_CUR, fragments);
        tag.putInt(N_MAX, max);
        writeTag(stack, tag);
        return stack;
    }

    public static String getMetalId(ItemStack stack) {
        CompoundTag tag = readTag(stack);
        return tag.getString(N_METAL).orElse("");
    }


    public static int getFragments(ItemStack stack) {
        CompoundTag tag = readTag(stack);
        return tag.getInt(N_CUR).orElse(0);
    }


    public static int getMax(ItemStack stack) {
        CompoundTag tag = readTag(stack);
        int v = tag.getInt(N_MAX).orElse(9);
        return v <= 0 ? 9 : v;
    }


    // ✅ 1.21.x 쪽 시그니처

    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        String metal = getMetalId(stack);
        int cur = getFragments(stack);
        int max = getMax(stack);

        if (metal != null && !metal.isBlank()) {
            tooltip.add(Component.literal("금속: " + metal));
        }
        tooltip.add(Component.literal("용량: " + cur + " / " + max));

        int missing = Math.max(0, max - cur);
        if (missing > 0) {
            tooltip.add(Component.literal("스탯 패널티: -" + missing + " (임시)"));
        }
    }

}
