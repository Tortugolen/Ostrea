package com.tortugolen.ostrea.Enchantments;

import com.tortugolen.ostrea.Init.InitItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ReductionEnchantment extends Enchantment {
    public ReductionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pEquipmentSlots) {
        super(pRarity, pCategory, pEquipmentSlots);
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.is(InitItems.PEARL_NECKLACE.get());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack pStack) {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
