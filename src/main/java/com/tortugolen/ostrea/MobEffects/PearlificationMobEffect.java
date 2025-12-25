package com.tortugolen.ostrea.MobEffects;

import com.tortugolen.ostrea.Init.InitMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Iterator;

public class PearlificationMobEffect extends MobEffect {
    public PearlificationMobEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Iterator<MobEffectInstance> iterator = livingEntity.getActiveEffects().iterator();

        if (amplifier == 0) {
            while (iterator.hasNext()) {
                MobEffectInstance effect = iterator.next();
                if (effect.getEffect() != this) {
                    livingEntity.removeEffect(effect.getEffect());
                }
            }
        }

        if (amplifier == 1) {
            while (iterator.hasNext()) {
                MobEffectInstance effect = iterator.next();
                if (effect.getEffect() != this && !effect.getEffect().isBeneficial()) {
                    livingEntity.removeEffect(effect.getEffect());
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
