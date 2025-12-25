package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.AbstractCultRecipes;
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

public class AbstractCultJEI implements IRecipeCategory<AbstractCultRecipes> {
    public static final ResourceLocation UID = new ResourceLocation(Ostrea.MOD_ID, "abstract_cult");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/jei/abstract_cult.png");
    public static final RecipeType<AbstractCultRecipes> ABSTRACT_CULT_TYPE = new RecipeType<>(UID, AbstractCultRecipes.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawableAnimated arrowAnimated;

    public AbstractCultJEI(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 136, 72);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(InitItems.DEEPSLATE_ALTAR_ITEM.get()));
        this.arrow = helper.createDrawable(TEXTURE, 136, 0, 22, 16);
        this.arrowAnimated = helper.createAnimatedDrawable((IDrawableStatic) arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<AbstractCultRecipes> getRecipeType() {
        return ABSTRACT_CULT_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.ostrea.abstract_cult");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractCultRecipes recipe, IFocusGroup focuses) {
        int size = recipe.getIngredients().size();

        builder.addSlot(RecipeIngredientRole.INPUT, 28, 28).addIngredients(recipe.getIngredients().get(0));

        if (size == 5) {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 1).addIngredients(recipe.getIngredients().get(1));
            builder.addSlot(RecipeIngredientRole.INPUT, 55, 28).addIngredients(recipe.getIngredients().get(2));
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 55).addIngredients(recipe.getIngredients().get(3));
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 28).addIngredients(recipe.getIngredients().get(4));
        }

        if (size == 9) {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 1).addIngredients(recipe.getIngredients().get(1));
            builder.addSlot(RecipeIngredientRole.INPUT, 55, 1).addIngredients(recipe.getIngredients().get(2));
            builder.addSlot(RecipeIngredientRole.INPUT, 55, 28).addIngredients(recipe.getIngredients().get(3));
            builder.addSlot(RecipeIngredientRole.INPUT, 55, 55).addIngredients(recipe.getIngredients().get(4));
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 55).addIngredients(recipe.getIngredients().get(5));
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 55).addIngredients(recipe.getIngredients().get(6));
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 28).addIngredients(recipe.getIngredients().get(7));
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(8));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 28).addItemStack(recipe.getResultItem(null));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 82, 3).addItemStack(InitItems.DEEPSLATE_ALTAR_ITEM.get().getDefaultInstance());
    }

    @Override
    public void draw(AbstractCultRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrowAnimated.draw(guiGraphics, 79, 28);
    }
}