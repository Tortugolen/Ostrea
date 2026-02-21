package com.tortugolen.ostrea.Events;

import com.tortugolen.ostrea.Entities.AncestralAxolotlEntity;
import com.tortugolen.ostrea.Entities.MossyAxolotlEntity;
import com.tortugolen.ostrea.Init.InitEntities;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ostrea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(InitEntities.MOSSY_AXOLOTL.get(), MossyAxolotlEntity.createAttributes().build());
        event.put(InitEntities.ANCESTRAL_AXOLOTL.get(), AncestralAxolotlEntity.createAttributes().add(Attributes.MAX_HEALTH, 20.0D).build());
    }
}