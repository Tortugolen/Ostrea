package com.tortugolen.ostrea.Items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FoilItem extends Item {
    public FoilItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
