package com.tortugolen.ostrea.Events;

import com.tortugolen.ostrea.Init.InitMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PearlificationHurtEvent {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity pEntity = event.getEntity();

        if (pEntity.hasEffect(InitMobEffects.PEARLIFICATION.get())) {
            pEntity.removeEffect(InitMobEffects.PEARLIFICATION.get());
        }
    }
}