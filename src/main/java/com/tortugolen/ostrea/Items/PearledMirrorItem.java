package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitSounds;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PearledMirrorItem extends Item {
    private static final Map<MobEffect, MobEffect> MOB_EFFECTS = new HashMap<>();

    static {
        MOB_EFFECTS.put(MobEffects.NIGHT_VISION, MobEffects.BLINDNESS);

        MOB_EFFECTS.put(MobEffects.MOVEMENT_SPEED, MobEffects.MOVEMENT_SLOWDOWN);
        MOB_EFFECTS.put(MobEffects.JUMP, MobEffects.MOVEMENT_SLOWDOWN);

        MOB_EFFECTS.put(MobEffects.REGENERATION, MobEffects.POISON);

        MOB_EFFECTS.put(MobEffects.DIG_SPEED, MobEffects.DIG_SLOWDOWN);
        MOB_EFFECTS.put(MobEffects.DAMAGE_BOOST, MobEffects.WEAKNESS);
        MOB_EFFECTS.put(MobEffects.SATURATION, MobEffects.HUNGER);
        MOB_EFFECTS.put(MobEffects.LUCK, MobEffects.UNLUCK);
        MOB_EFFECTS.put(MobEffects.HEAL, MobEffects.HARM);
        MOB_EFFECTS.put(MobEffects.SLOW_FALLING, MobEffects.LEVITATION);
        MOB_EFFECTS.put(MobEffects.HERO_OF_THE_VILLAGE, MobEffects.BAD_OMEN);


        MOB_EFFECTS.put(MobEffects.CONFUSION, MobEffects.NIGHT_VISION);
        MOB_EFFECTS.put(MobEffects.DARKNESS, MobEffects.NIGHT_VISION);
        MOB_EFFECTS.put(MobEffects.BLINDNESS, MobEffects.NIGHT_VISION);

        MOB_EFFECTS.put(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.MOVEMENT_SPEED);

        MOB_EFFECTS.put(MobEffects.WITHER, MobEffects.REGENERATION);
        MOB_EFFECTS.put(MobEffects.POISON, MobEffects.REGENERATION);

        MOB_EFFECTS.put(MobEffects.DIG_SLOWDOWN, MobEffects.DIG_SPEED);
        MOB_EFFECTS.put(MobEffects.WEAKNESS, MobEffects.DAMAGE_BOOST);
        MOB_EFFECTS.put(MobEffects.HUNGER, MobEffects.SATURATION);
        MOB_EFFECTS.put(MobEffects.UNLUCK, MobEffects.LUCK);
        MOB_EFFECTS.put(MobEffects.HARM, MobEffects.HEAL);
        MOB_EFFECTS.put(MobEffects.LEVITATION, MobEffects.SLOW_FALLING);
        MOB_EFFECTS.put(MobEffects.BAD_OMEN, MobEffects.HERO_OF_THE_VILLAGE);

    }

    public PearledMirrorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        Map<Enchantment, Integer> enchantments = pStack.getAllEnchantments();

        boolean hasSelectiveBlessing = enchantments.containsKey(InitEnchantments.SELECTIVE_BLESSING.get());
        boolean inverted = false;
        int count = 0;

        for (MobEffectInstance effectInstance : List.copyOf(pPlayer.getActiveEffects())) {
            MobEffect currentEffect = effectInstance.getEffect();

            if (!hasSelectiveBlessing) {
                if (MOB_EFFECTS.containsKey(currentEffect)) {
                    MobEffect invertedEffect = MOB_EFFECTS.get(currentEffect);
                    pPlayer.removeEffect(currentEffect);
                    pPlayer.addEffect(new MobEffectInstance(invertedEffect, effectInstance.getDuration(), effectInstance.getAmplifier()));
                    inverted = true;
                    count = count + 1;
                }
            } else {
                if (!currentEffect.isBeneficial()) {
                    if (MOB_EFFECTS.containsKey(currentEffect)) {
                        MobEffect invertedEffect = MOB_EFFECTS.get(currentEffect);
                        pPlayer.removeEffect(currentEffect);
                        pPlayer.addEffect(new MobEffectInstance(invertedEffect, effectInstance.getDuration(), effectInstance.getAmplifier()));
                        inverted = true;
                        count = count + 1;
                    }
                }
            }
        }

        if (!inverted) return InteractionResultHolder.fail(pStack);

        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), InitSounds.EFFECT_CLEAR.get(), SoundSource.PLAYERS, 1F, 1F);

        if (!pPlayer.isCreative()) {
            pStack.hurtAndBreak(4 * pPlayer.getActiveEffects().size(), pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
        }

        if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer && count > 0) {
            InitTriggers.ABSTRACT.trigger(pServerPlayer);
        }

        return InteractionResultHolder.success(pStack);
    }
}
