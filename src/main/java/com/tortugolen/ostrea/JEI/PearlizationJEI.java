package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import com.tortugolen.ostrea.Recipes.PearlizationRecipes;
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

public class PearlizationJEI implements IRecipeCategory<PearlizationRecipes> {
    public static final ResourceLocation UID = new ResourceLocation(Ostrea.MOD_ID, "pearlization");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/jei/crusher.png");
    public static final RecipeType<PearlizationRecipes> PEARLIZATION_TYPE = new RecipeType<>(UID, PearlizationRecipes.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawableAnimated arrowAnimated;

    public PearlizationJEI(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 82, 26);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(InitItems.OYSTER_ITEM.get()));
        this.arrow = helper.createDrawable(TEXTURE, 82, 0, 22, 16);
        this.arrowAnimated = helper.createAnimatedDrawable((IDrawableStatic) arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<PearlizationRecipes> getRecipeType() {
        return PEARLIZATION_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, PearlizationRecipes recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(PearlizationRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrowAnimated.draw(guiGraphics, 25, 5);
    }
}