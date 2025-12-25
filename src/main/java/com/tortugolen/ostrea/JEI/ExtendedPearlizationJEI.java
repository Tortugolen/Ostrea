package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
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
import org.jetbrains.annotations.Nullable;

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
    private final IDrawable arrow;
    private final IDrawableAnimated arrowAnimated;
    private final IDrawable CaCO3Bar;
    private final IDrawableAnimated CaCO3BarAnimated;
    private final IDrawable CaCO3Cycles;
    private final IDrawableAnimated CaCO3CyclesAnimated;

    public ExtendedPearlizationJEI(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 139, 73);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(InitItems.MECHANICAL_OYSTER_ITEM.get()));
        this.arrow = helper.createDrawable(TEXTURE, 139, 0, 59, 16);
        this.arrowAnimated = helper.createAnimatedDrawable((IDrawableStatic) arrow, 640, IDrawableAnimated.StartDirection.LEFT, false);
        this.CaCO3Bar = helper.createDrawable(TEXTURE, 139, 16, 52, 4);
        this.CaCO3BarAnimated = helper.createAnimatedDrawable((IDrawableStatic) CaCO3Bar, 320, IDrawableAnimated.StartDirection.LEFT, true);
        this.CaCO3Cycles = helper.createDrawable(TEXTURE, 139, 36, 34, 34);
        this.CaCO3CyclesAnimated = helper.createAnimatedDrawable((IDrawableStatic) CaCO3Cycles, 160, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public RecipeType<ExtendedPearlizationRecipes> getRecipeType() {
        return EXTENDED_PEARLIZATION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.ostrea.pearlization");
    }

    @Nullable
    @Override
    public IDrawable getBackground() {
        return background;
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
        builder.addSlot(RecipeIngredientRole.INPUT, 64, 42).addItemStacks(FUELS);
        builder.addSlot(RecipeIngredientRole.INPUT, 118, 56).addItemStacks(IMPURITIES);
    }

    @Override
    public void draw(ExtendedPearlizationRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrowAnimated.draw(guiGraphics, 45, 30);
        CaCO3Bar.draw(guiGraphics, 46, 67);
        CaCO3CyclesAnimated.draw(guiGraphics, 1, 21);
        guiGraphics.blit(TEXTURE, 55, 19, 139, 20, 16, 16);
        guiGraphics.blit(TEXTURE, 73, 19, 139, 20, 16, 16);
    }
}