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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class InfuseRecipes implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final MobEffect effect;
    private final ItemStack result;
    private final ResourceLocation id;

    public InfuseRecipes(NonNullList<Ingredient> inputItems, MobEffect effect, ItemStack result, ResourceLocation id) {
        this.inputItems = inputItems;
        this.effect = effect;
        this.result = result;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(pContainer.getItem(4))
            && inputItems.get(1).test(pContainer.getItem(5));
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
        return InfuseRecipes.Serializer.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public MobEffect getEffect() {
        return effect;
    }

    public static class Type implements RecipeType<InfuseRecipes> {
        public static final InfuseRecipes.Type INSTANCE = new InfuseRecipes.Type();
        public static final String ID = "infuse";
    }

    public static class Serializer implements RecipeSerializer<InfuseRecipes> {
        public static final InfuseRecipes.Serializer INSTANCE = new InfuseRecipes.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Ostrea.MOD_ID, "infuse");

        @Override
        public InfuseRecipes fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

            String effectId = GsonHelper.getAsString(pSerializedRecipe, "effect");
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new InfuseRecipes(inputs, effect, result, pRecipeId);
        }

        @Override
        public @Nullable InfuseRecipes fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack result = pBuffer.readItem();

            ResourceLocation effectId = pBuffer.readResourceLocation();
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);

            return new InfuseRecipes(inputs, effect, result, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, InfuseRecipes pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeResourceLocation(ForgeRegistries.MOB_EFFECTS.getKey(pRecipe.effect));
        }
    }
}