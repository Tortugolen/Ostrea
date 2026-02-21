package com.tortugolen.ostrea.Compat.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Items.StatusferItem;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Recipes.InfuseRecipes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfuseJEI implements IRecipeCategory<InfuseRecipes> {
    public static final ResourceLocation UID = new ResourceLocation(Ostrea.MOD_ID, "infuse");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/jei/soul_table.png");
    public static final RecipeType<InfuseRecipes> INFUSE_TYPE = new RecipeType<>(UID, InfuseRecipes.class);

    private static final List<ItemStack> FUELS = List.of(
            new ItemStack(Items.EXPERIENCE_BOTTLE),
            new ItemStack(Items.ENCHANTED_BOOK),
            new ItemStack(Items.ENCHANTED_GOLDEN_APPLE)
    );

    private static final List<ItemStack> FUEL_REMAINDERS = List.of(
            new ItemStack(Items.GLASS_BOTTLE),
            new ItemStack(Items.BOOK),
            new ItemStack(Items.GOLDEN_APPLE)
    );

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawableAnimated arrowAnimated;
    private final IDrawable arrowSouls;
    private final IDrawableAnimated arrowSoulsAnimated;
    private final IDrawable arrowEffectSouls;
    private final IDrawableAnimated arrowEffectSoulsAnimated;
    private final IDrawable soulsBar;
    private final IDrawableAnimated soulsBarAnimated;

    public InfuseJEI(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 149, 66);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(InitItems.SOUL_TABLE_ITEM.get()));
        this.arrow = helper.createDrawable(TEXTURE, 149, 24, 22, 16);
        this.arrowAnimated = helper.createAnimatedDrawable((IDrawableStatic) arrow, 640, IDrawableAnimated.StartDirection.LEFT, false);
        this.arrowSouls = helper.createDrawable(TEXTURE, 149, 8, 8, 16);
        this.arrowSoulsAnimated = helper.createAnimatedDrawable((IDrawableStatic) arrowSouls, 40, IDrawableAnimated.StartDirection.TOP, false);
        this.arrowEffectSouls = helper.createDrawable(TEXTURE, 149, 0, 22, 8);
        this.arrowEffectSoulsAnimated = helper.createAnimatedDrawable((IDrawableStatic) arrowEffectSouls, 160, IDrawableAnimated.StartDirection.LEFT, false);
        this.soulsBar = helper.createDrawable(TEXTURE, 0, 66, 4, 64);
        this.soulsBarAnimated = helper.createAnimatedDrawable((IDrawableStatic) soulsBar, 160, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public RecipeType<InfuseRecipes> getRecipeType() {
        return INFUSE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.ostrea.infuse");
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
    public void setRecipe(IRecipeLayoutBuilder builder, InfuseRecipes recipe, IFocusGroup focuses) {
        ItemStack effectStatusfer = getStatusferForEffect(recipe.getEffect());
        CompoundTag pTag = effectStatusfer.getOrCreateTag();
        pTag.putString(StatusferItem.EFFECT, ForgeRegistries.MOB_EFFECTS.getKey(recipe.getEffect()).toString());

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 7).addItemStacks(FUELS);
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 43).addItemStacks(FUEL_REMAINDERS);
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 20).addItemStack(effectStatusfer);
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 43).addItemStack(new ItemStack(InitItems.STATUSFER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 7).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 43).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 25).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(InfuseRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrowAnimated.draw(guiGraphics, 92, 25);
        arrowSoulsAnimated.draw(guiGraphics, 6, 25);
        arrowEffectSoulsAnimated.draw(guiGraphics, 34, 6);
        soulsBarAnimated.draw(guiGraphics, 27, 1);
        guiGraphics.blit(TEXTURE, 59, 1, getEffectSoulsBarEffect(recipe.getEffect()), 66, 4, 64);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, InfuseRecipes recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isHovering(mouseX, mouseY, 58, 0, 6, 66)) {
            tooltip.add(Component.empty().append(recipe.getEffect().getDisplayName().copy().withStyle(style -> style.withColor(recipe.getEffect().getColor()))));
        }
    }

    private ItemStack getStatusferForEffect(MobEffect effect) {
        ResourceLocation effectKey = ForgeRegistries.MOB_EFFECTS.getKey(effect);

        if (effectKey != null) {
            if (effectKey.equals(new ResourceLocation("minecraft", "regeneration"))) {
                return new ItemStack(InitItems.REGENERATION_STATUSFER.get());
            }
        }

        ItemStack genericStatusfer = new ItemStack(InitItems.STATUSFER.get());
        CompoundTag tag = genericStatusfer.getOrCreateTag();
        tag.putString("effect", ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
        return genericStatusfer;
    }

    private int getEffectSoulsBarEffect(MobEffect effect) {
        if (effect == MobEffects.REGENERATION) return 4;

        return 0;
    }

    private boolean isHovering(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}