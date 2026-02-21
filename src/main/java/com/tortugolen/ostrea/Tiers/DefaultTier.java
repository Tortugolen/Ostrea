package com.tortugolen.ostrea.Tiers;

import com.tortugolen.ostrea.Init.InitItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class DefaultTier implements Tier {
    @Override
    public int getUses() {
        return 64;
    }

    @Override
    public float getSpeed() {
        return 1;
    }

    @Override
    public float getAttackDamageBonus() {
        return 1;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 5;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.IRON_INGOT);
    }
}
