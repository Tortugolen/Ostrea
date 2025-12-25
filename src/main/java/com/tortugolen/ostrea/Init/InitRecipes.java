package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.AbstractCultRecipes;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
import com.tortugolen.ostrea.Recipes.PearlizationRecipes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Ostrea.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PearlizationRecipes>> PEARLIZATION = SERIALIZERS.register(
            "pearlization", () -> PearlizationRecipes.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ExtendedPearlizationRecipes>> EXTENDED_PEARLIZATION = SERIALIZERS.register(
            "extended_pearlization", () -> ExtendedPearlizationRecipes.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<CrushRecipes>> CRUSH = SERIALIZERS.register(
            "crush", () -> CrushRecipes.Serializer.INSTANCE);

    //Cults

    public static final RegistryObject<RecipeSerializer<AbstractCultRecipes>> ABSTRACT_CULT = SERIALIZERS.register(
            "abstract_cult", () -> AbstractCultRecipes.Serializer.INSTANCE);

}
