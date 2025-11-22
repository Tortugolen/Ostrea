package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CrushJEI implements IRecipeCategory<CrushRecipes> {
    public static final ResourceLocation UID = new ResourceLocation(Ostrea.MOD_ID, "crush");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/jei/crusher.png");
    public static final RecipeType<CrushRecipes> CRUSH_TYPE = new RecipeType<>(UID, CrushRecipes.class);
    private final IDrawable background;
    private final IDrawable icon;

    public CrushJEI(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 82, 26);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(InitItems.CRUSHER_ITEM.get()));
    }

    @Override
    public RecipeType<CrushRecipes> getRecipeType() {
        return CRUSH_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.ostrea.crush");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrushRecipes recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(CrushRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }
}