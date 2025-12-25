package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.MobEffects.PearlificationMobEffect;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Ostrea.MOD_ID);

    public static final RegistryObject<MobEffect> PEARLIFICATION = MOB_EFFECTS.register("pearlification", () -> new PearlificationMobEffect(MobEffectCategory.BENEFICIAL, 0xFFFFE6));
}
