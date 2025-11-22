package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
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

import java.util.List;

public class ExtendedPearlizationJEI implements IRecipeCategory<ExtendedPearlizationRecipes> {
    public static final ResourceLocation UID = new ResourceLocation(Ostrea.MOD_ID, "extended_pearlization");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/jei/mechanical_oyster.png");
    public static final RecipeType<ExtendedPearlizationRecipes> EXTENDED_PEARLIZATION_TYPE = new RecipeType<>(UID, ExtendedPearlizationRecipes.class);

    private static final List<ItemStack> FUELS = List.of(
            new ItemStack(InitItems.CALCIUM_CARBONATE.get()),
            new ItemStack(InitItems.ARAGONITE_POWDER.get())
    );

    private static final List<ItemStack> IMPURITIES = List.of(
            new ItemStack(InitItems.IMPURITIES.get()));

    private final IDrawable background;
    private final IDrawable icon;

    public ExtendedPearlizationJEI(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 139, 73);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(InitItems.MECHANICAL_OYSTER_ITEM.get()));
    }

    @Override
    public RecipeType<ExtendedPearlizationRecipes> getRecipeType() {
        return EXTENDED_PEARLIZATION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.ostrea.pearlization");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ExtendedPearlizationRecipes recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 30).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 1).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 1).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 30).addItemStack(recipe.getResultItem(null));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 42).addItemStacks(FUELS);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 118, 56).addItemStacks(IMPURITIES);
    }

    @Override
    public void draw(ExtendedPearlizationRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        //guiGraphics.blit(TEXTURE, 45, 30, 139,0,);
        //guiGraphics.blit(TEXTURE, 55, 19, 139, 20, 16, 16);
        //guiGraphics.blit(TEXTURE, 73, 19, 139, 20, 16, 16);
    }
}