package com.tortugolen.ostrea.Enchantments;

import com.tortugolen.ostrea.Init.InitItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ShellOpenerEnchantment extends Enchantment {
    public ShellOpenerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pEquipmentSlots) {
        super(pRarity, pCategory, pEquipmentSlots);
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof TieredItem || pStack.is(InitItems.SHELLFISH_KNIFE.get()) || pStack.is(InitItems.NACRE_DAGGER.get());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack pStack) {
        return false;
    }
}
