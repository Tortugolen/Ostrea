package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.AbstractCultRecipes;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
import com.tortugolen.ostrea.Recipes.PearlizationRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
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

        //Cults

        registration.addRecipeCategories(new AbstractCultJEI(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, EnchantedBooksJEI.getAllEnchantedBooks());

        List<PearlizationRecipes> pearlizationRecipes = recipeManager.getAllRecipesFor(PearlizationRecipes.Type.INSTANCE);
        List<ExtendedPearlizationRecipes> extendedPearlizationRecipes = recipeManager.getAllRecipesFor(ExtendedPearlizationRecipes.Type.INSTANCE);
        List<CrushRecipes> crushRecipes = recipeManager.getAllRecipesFor(CrushRecipes.Type.INSTANCE);

        registration.addRecipes(PearlizationJEI.PEARLIZATION_TYPE, pearlizationRecipes);
        registration.addRecipes(ExtendedPearlizationJEI.EXTENDED_PEARLIZATION_TYPE, extendedPearlizationRecipes);
        registration.addRecipes(CrushJEI.CRUSH_TYPE, crushRecipes);

        //Cults

        List<AbstractCultRecipes> abstractCultRecipes = recipeManager.getAllRecipesFor(AbstractCultRecipes.Type.INSTANCE);

        registration.addRecipes(AbstractCultJEI.ABSTRACT_CULT_TYPE, abstractCultRecipes);
    }
}