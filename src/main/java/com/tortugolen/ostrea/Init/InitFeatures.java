package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Features.AragoniteFeature;
import com.tortugolen.ostrea.Features.OysterFeature;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Ostrea.MOD_ID);

    public static final RegistryObject<Feature<RandomPatchConfiguration>> OYSTER = FEATURES.register(
            "oyster", () -> new OysterFeature(RandomPatchConfiguration.CODEC));

    public static final RegistryObject<Feature<RandomPatchConfiguration>> ARAGONITE = FEATURES.register(
            "aragonite", () -> new AragoniteFeature(RandomPatchConfiguration.CODEC));

}
