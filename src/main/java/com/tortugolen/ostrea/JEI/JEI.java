package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
import com.tortugolen.ostrea.Recipes.PearlizationRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Ostrea.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PearlizationJEI(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ExtendedPearlizationJEI(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrushJEI(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<PearlizationRecipes> pearlizationRecipes = recipeManager.getAllRecipesFor(PearlizationRecipes.Type.INSTANCE);
        registration.addRecipes(PearlizationJEI.PEARLIZATION_TYPE, pearlizationRecipes);
        List<ExtendedPearlizationRecipes> extendedPearlizationRecipes = recipeManager.getAllRecipesFor(ExtendedPearlizationRecipes.Type.INSTANCE);
        registration.addRecipes(ExtendedPearlizationJEI.EXTENDED_PEARLIZATION_TYPE, extendedPearlizationRecipes);
        List<CrushRecipes> crushRecipes = recipeManager.getAllRecipesFor(CrushRecipes.Type.INSTANCE);
        registration.addRecipes(CrushJEI.CRUSH_TYPE, crushRecipes);
    }

    /*@Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MechanicalOysterScreen.class, 60, 30, 20, 30,
                ExtendedPearlizationCategory.EXTENDED_PEARLIZATION_TYPE);
    }*/
}