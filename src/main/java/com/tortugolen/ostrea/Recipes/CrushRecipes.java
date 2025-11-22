package com.tortugolen.ostrea.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CrushRecipes implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack result;
    private final ResourceLocation id;

    public CrushRecipes(NonNullList<Ingredient> inputItems, ItemStack result, ResourceLocation id) {
        this.inputItems = inputItems;
        this.result = result;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrushRecipes.Serializer.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public RecipeType<?> getType() {
        return CrushRecipes.Type.INSTANCE;
    }

    public static class Type implements RecipeType<CrushRecipes> {
        public static final CrushRecipes.Type INSTANCE = new CrushRecipes.Type();
        public static final String ID = "crush";
    }

    public static class Serializer implements RecipeSerializer<CrushRecipes> {
        public static final CrushRecipes.Serializer INSTANCE = new CrushRecipes.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Ostrea.MOD_ID, "crush");

        @Override
        public CrushRecipes fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new CrushRecipes(inputs, result, pRecipeId);
        }

        @Override
        public @Nullable CrushRecipes fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack result = pBuffer.readItem();
            return new CrushRecipes(inputs, result, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CrushRecipes pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}