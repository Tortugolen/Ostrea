package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Triggers.AbstractTrigger;
import com.tortugolen.ostrea.Triggers.EffectRemovedTrigger;
import com.tortugolen.ostrea.Triggers.EnchantedItemTrigger;
import com.tortugolen.ostrea.Triggers.ItemUsedOnBlockTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class InitTriggers {
    public static final AbstractTrigger ABSTRACT = new AbstractTrigger();
    public static final EnchantedItemTrigger ENCHANTED_ITEM = new EnchantedItemTrigger();
    public static final EffectRemovedTrigger EFFECT_REMOVED = new EffectRemovedTrigger();
    public static final ItemUsedOnBlockTrigger ITEM_USED_ON_BLOCK = new ItemUsedOnBlockTrigger();

    public static void register() {
        CriteriaTriggers.register(ABSTRACT);
        CriteriaTriggers.register(ENCHANTED_ITEM);
        CriteriaTriggers.register(EFFECT_REMOVED);
        CriteriaTriggers.register(ITEM_USED_ON_BLOCK);
    }
}
