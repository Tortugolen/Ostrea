package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.AbstractCultRecipes;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
import com.tortugolen.ostrea.Recipes.PearlizationRecipes;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Ostrea.MOD_ID);

    public static final RegistryObject<RecipeType<PearlizationRecipes>> PEARLIZATION = RECIPE_TYPES.register(
            "pearlization", () -> PearlizationRecipes.Type.INSTANCE);
    public static final RegistryObject<RecipeType<ExtendedPearlizationRecipes>> EXTENDED_PEARLIZATION = RECIPE_TYPES.register(
            "extended_pearlization", () -> ExtendedPearlizationRecipes.Type.INSTANCE);
    public static final RegistryObject<RecipeType<CrushRecipes>> CRUSH = RECIPE_TYPES.register(
            "crush", () -> CrushRecipes.Type.INSTANCE);

    //Cults

    public static final RegistryObject<RecipeType<AbstractCultRecipes>> ABSTRACT_CULT = RECIPE_TYPES.register(
            "abstract_cult", () -> AbstractCultRecipes.Type.INSTANCE);

}
