package com.tortugolen.ostrea.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;

public class AbstractCultRecipes implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack result;
    private final ResourceLocation id;
    private final int size;
    private final String cult;
    private CompoundTag inputNbt = new CompoundTag();
    private CompoundTag resultNbt = new CompoundTag();

    public AbstractCultRecipes(NonNullList<Ingredient> inputItems, ItemStack result, ResourceLocation id, int size, String cult) {
        this.inputItems = inputItems;
        this.result = result;
        this.id = id;
        this.size = size;
        this.cult = cult;
    }

    public AbstractCultRecipes(NonNullList<Ingredient> inputItems, ItemStack result, ResourceLocation id, int size, String cult, CompoundTag inputNbt, CompoundTag resultNbt) {
        this.inputItems = inputItems;
        this.result = result;
        this.id = id;
        this.size = size;
        this.cult = cult;
        this.inputNbt = inputNbt;
        this.resultNbt = resultNbt;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        if (pContainer.getContainerSize() != this.inputItems.size()) {
            return false;
        }

        for (int i = 0; i < inputItems.size(); i++) {
            ItemStack pStack = pContainer.getItem(i);
            Ingredient ingredient = inputItems.get(i);

            if (!ingredient.test(pStack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        ItemStack resultStack = result.copy();

        if (!resultNbt.isEmpty()) {
            CompoundTag tag = resultStack.getOrCreateTag();
            tag.merge(resultNbt);
        }

        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        ItemStack resultStack = result.copy();

        if (!resultNbt.isEmpty()) {
            CompoundTag tag = resultStack.getOrCreateTag();
            tag.merge(resultNbt);
        }

        return resultStack;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AbstractCultRecipes.Serializer.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public String getCult() {
        return cult;
    }

    public CompoundTag getInputNbt() {
        return this.inputNbt;
    }

    public CompoundTag getResultNbt() {
        return this.resultNbt;
    }

    public void setInputNbt(CompoundTag nbt) {
        this.inputNbt = nbt;
    }

    public void setResultNbt(CompoundTag nbt) {
        this.resultNbt = nbt;
    }

    public static class Type implements RecipeType<AbstractCultRecipes> {
        public static final AbstractCultRecipes.Type INSTANCE = new AbstractCultRecipes.Type();
        public static final String ID = "abstract_cult";
    }

    public static class Serializer implements RecipeSerializer<AbstractCultRecipes> {
        public static final AbstractCultRecipes.Serializer INSTANCE = new AbstractCultRecipes.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Ostrea.MOD_ID, "abstract_cult");

        @Override
        public AbstractCultRecipes fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            int size = GsonHelper.getAsInt(pSerializedRecipe, "size");
            String cult = GsonHelper.getAsString(pSerializedRecipe, "cult");

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            CompoundTag inputNbt = new CompoundTag();

            for(int i = 0; i < inputs.size(); i++) {
                JsonElement ingredientElement = ingredients.get(i);
                inputs.set(i, Ingredient.fromJson(ingredientElement));

                if (i == 0 && ingredientElement.isJsonObject()) {
                    JsonObject ingredientObj = ingredientElement.getAsJsonObject();
                    if (ingredientObj.has("nbt")) {
                        inputNbt = parseNbtFromJson(ingredientObj.get("nbt"));
                    }
                }
            }

            CompoundTag resultNbt = new CompoundTag();
            if (pSerializedRecipe.has("result")) {
                JsonObject resultObj = GsonHelper.getAsJsonObject(pSerializedRecipe, "result");
                if (resultObj.has("nbt")) {
                    resultNbt = parseNbtFromJson(resultObj.get("nbt"));
                }
            }

            return new AbstractCultRecipes(inputs, result, pRecipeId, size, cult, inputNbt, resultNbt);
        }

        private static CompoundTag parseNbtFromJson(JsonElement nbtElement) {
            CompoundTag tag = new CompoundTag();
            if (nbtElement.isJsonObject()) {
                JsonObject nbtObj = nbtElement.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : nbtObj.entrySet()) {
                    JsonElement value = entry.getValue();
                    if (value.isJsonPrimitive()) {
                        JsonPrimitive prim = value.getAsJsonPrimitive();
                        if (prim.isString()) {
                            tag.putString(entry.getKey(), prim.getAsString());
                        } else if (prim.isNumber()) {
                            if (prim.getAsString().contains(".")) {
                                tag.putFloat(entry.getKey(), prim.getAsFloat());
                            } else {
                                tag.putInt(entry.getKey(), prim.getAsInt());
                            }
                        } else if (prim.isBoolean()) {
                            tag.putBoolean(entry.getKey(), prim.getAsBoolean());
                        }
                    } else if (value.isJsonObject()) {
                        tag.put(entry.getKey(), parseNbtFromJson(value));
                    }
                }
            }
            return tag;
        }

        @Override
        public @Nullable AbstractCultRecipes fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int ingredientsSize = pBuffer.readInt();
            int size = pBuffer.readInt();
            String cult = pBuffer.readUtf(32767);

            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredientsSize, Ingredient.EMPTY);

            for (int i = 0; i < ingredientsSize; i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack result = pBuffer.readItem();

            CompoundTag inputNbt = pBuffer.readNbt();
            if (inputNbt == null) {
                inputNbt = new CompoundTag();
            }

            CompoundTag resultNbt = pBuffer.readNbt();
            if (resultNbt == null) {
                resultNbt = new CompoundTag();
            }

            return new AbstractCultRecipes(inputs, result, pRecipeId, size, cult, inputNbt, resultNbt);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AbstractCultRecipes pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());
            pBuffer.writeInt(pRecipe.size);
            pBuffer.writeUtf(pRecipe.cult);

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.result, false);

            pBuffer.writeNbt(pRecipe.inputNbt);

            pBuffer.writeNbt(pRecipe.resultNbt);
        }
    }
}