package com.tortugolen.ostrea.Base;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

public class NBTIngredient extends AbstractIngredient {

    private final ItemStack stack;

    public NBTIngredient(ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return ItemStack.isSameItemSameTags(this.stack, input);
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{this.stack};
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "ostrea:nbt_ingredient");
        json.addProperty("item", net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(this.stack.getItem()).toString());

        if (this.stack.hasTag()) {
            json.addProperty("nbt", this.stack.getTag().toString());
        }

        return json;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public static class Serializer implements IIngredientSerializer<NBTIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public NBTIngredient parse(FriendlyByteBuf buffer) {
            return new NBTIngredient(buffer.readItem());
        }

        @Override
        public NBTIngredient parse(JsonObject json) {
            ItemStack stack = net.minecraftforge.common.crafting.CraftingHelper.getItemStack(json, true);
            return new NBTIngredient(stack);
        }

        @Override
        public void write(FriendlyByteBuf buffer, NBTIngredient ingredient) {
            buffer.writeItem(ingredient.stack);
        }
    }
}