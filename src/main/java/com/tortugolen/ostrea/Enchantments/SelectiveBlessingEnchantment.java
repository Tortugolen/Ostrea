package com.tortugolen.ostrea.Enchantments;

import com.tortugolen.ostrea.Init.InitItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SelectiveBlessingEnchantment extends Enchantment {
    public SelectiveBlessingEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pEquipmentSlots) {
        super(pRarity, pCategory, pEquipmentSlots);
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.is(InitItems.PEARL_NECKLACE.get()) || pStack.is(InitItems.PEARL_RING.get()) || pStack.is(InitItems.PEARLED_MIRROR.get());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }
}
