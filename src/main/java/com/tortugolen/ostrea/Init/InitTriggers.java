package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Triggers.Abstract.*;
import com.tortugolen.ostrea.Triggers.EffectRemovedTrigger;
import com.tortugolen.ostrea.Triggers.EnchantedItemTrigger;
import com.tortugolen.ostrea.Triggers.ItemUsedOnBlockTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class InitTriggers {
    public static final AbstractTrigger0 ABSTRACT0 = new AbstractTrigger0();
    public static final AbstractTrigger1 ABSTRACT1 = new AbstractTrigger1();
    public static final AbstractTrigger2 ABSTRACT2 = new AbstractTrigger2();
    public static final AbstractTrigger3 ABSTRACT3 = new AbstractTrigger3();
    public static final AbstractTrigger4 ABSTRACT4 = new AbstractTrigger4();
    public static final AbstractTrigger5 ABSTRACT5 = new AbstractTrigger5();
    public static final AbstractTrigger6 ABSTRACT6 = new AbstractTrigger6();
    public static final EnchantedItemTrigger ENCHANTED_ITEM = new EnchantedItemTrigger();
    public static final EffectRemovedTrigger EFFECT_REMOVED = new EffectRemovedTrigger();
    public static final ItemUsedOnBlockTrigger ITEM_USED_ON_BLOCK = new ItemUsedOnBlockTrigger();

    public static void register() {
        CriteriaTriggers.register(ABSTRACT0);
        CriteriaTriggers.register(ABSTRACT1);
        CriteriaTriggers.register(ABSTRACT2);
        CriteriaTriggers.register(ABSTRACT3);
        CriteriaTriggers.register(ABSTRACT4);
        CriteriaTriggers.register(ABSTRACT5);
        CriteriaTriggers.register(ABSTRACT6);
        CriteriaTriggers.register(ENCHANTED_ITEM);
        CriteriaTriggers.register(EFFECT_REMOVED);
        CriteriaTriggers.register(ITEM_USED_ON_BLOCK);
    }
}
