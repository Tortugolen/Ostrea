package com.tortugolen.ostrea.JEI;

import com.tortugolen.ostrea.Init.InitItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class EffectPearls {

    private static final ResourceLocation[] ALL_EFFECTS = {
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.MOVEMENT_SPEED),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.MOVEMENT_SLOWDOWN),   // Stray
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.DIG_SLOWDOWN),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.DIG_SPEED),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.DAMAGE_BOOST),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.HEAL),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.HARM),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.JUMP),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.CONFUSION),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.REGENERATION),        // Axolotl
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.DAMAGE_RESISTANCE),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.FIRE_RESISTANCE),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.WATER_BREATHING),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.INVISIBILITY),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.BLINDNESS),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.NIGHT_VISION),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.HUNGER),              // Husk
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.WEAKNESS),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.POISON),              // Cave Spider / Pufferfish
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.WITHER),              // Wither Skeleton / Wither
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.ABSORPTION),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.SATURATION),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.GLOWING),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.LEVITATION),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.LUCK),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.UNLUCK),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.SLOW_FALLING),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.CONDUIT_POWER),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.BAD_OMEN),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.DOLPHINS_GRACE),      // Dolphin
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.HERO_OF_THE_VILLAGE),
            ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.DARKNESS),            // Warden
    };

    public static List<ItemStack> getAllReceptaclePearlVariants() {
        List<ItemStack> variants = new ArrayList<>();

        for (ResourceLocation effectId : ALL_EFFECTS) {
            if (effectId != null) {
                ItemStack fullStack = new ItemStack(InitItems.RECEPTACLE_PEARL.get());
                CompoundTag fullTag = fullStack.getOrCreateTag();
                fullTag.putString("stored_effect", effectId.toString());
                fullTag.putInt("effect_amount", 64);
                variants.add(fullStack);
            }
        }

        return variants;
    }

    public static List<ItemStack> getAllStatusferVariants() {
        List<ItemStack> variants = new ArrayList<>();

        for (ResourceLocation effectId : ALL_EFFECTS) {
            if (effectId != null) {
                ItemStack stack = new ItemStack(InitItems.STATUSFER.get());
                CompoundTag tag = stack.getOrCreateTag();
                tag.putString("effect", effectId.toString());
                variants.add(stack);
            }
        }

        return variants;
    }
}