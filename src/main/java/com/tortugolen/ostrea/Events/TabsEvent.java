package com.tortugolen.ostrea.Events;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ostrea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TabsEvent {
    @SubscribeEvent
    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(InitItems.MOSSY_AXOLOTL_SPAWN_EGG.get());
            event.accept(InitItems.ANCESTRAL_AXOLOTL_SPAWN_EGG.get());
        }
    }
}
