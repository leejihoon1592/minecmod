package com.jangi10.mineblacksmith.recipe;

import com.jangi10.mineblacksmith.MineBlacksmith;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MineBlacksmith.MODID);

    // ✅ 코크스 오븐 전용 "요리(화로계열)" 레시피 타입
    public static final DeferredHolder<RecipeType<?>, RecipeType<?>> COKE_OVEN =
            RECIPE_TYPES.register("coke_oven", () -> new RecipeType<>() {});
}
