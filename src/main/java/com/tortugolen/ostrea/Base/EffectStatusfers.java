package com.tortugolen.ostrea.Base;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Items.StatusferItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface EffectStatusfers {
     static void addRegenerationStatusfer(CreativeModeTab.Output output, String effect) {
        ItemStack pStack = new ItemStack(InitItems.REGENERATION_STATUSFER.get());
        CompoundTag tag = pStack.getOrCreateTag();
        tag.putString(StatusferItem.EFFECT, effect);
        output.accept(pStack);
    }
}
