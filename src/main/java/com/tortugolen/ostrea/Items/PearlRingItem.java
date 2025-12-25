package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitMobEffects;
import com.tortugolen.ostrea.Init.InitSounds;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.Map;

public class PearlRingItem extends Item {
    public PearlRingItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        Map<Enchantment, Integer> enchantments = pStack.getAllEnchantments();

        MobEffectInstance pearlificationEffect = pPlayer.getEffect(InitMobEffects.PEARLIFICATION.get());

        boolean hasSelectiveBlessing = enchantments.containsKey(InitEnchantments.SELECTIVE_BLESSING.get());
        boolean hasPearlification = pearlificationEffect != null;

        if (!hasSelectiveBlessing) {
            if (!hasPearlification) {
                pPlayer.addEffect(new MobEffectInstance(InitMobEffects.PEARLIFICATION.get(), 1200, 0));
            } else {
                pPlayer.addEffect(new MobEffectInstance(InitMobEffects.PEARLIFICATION.get(), pearlificationEffect.getDuration() + 1200, 0));
            }
        } else {
            if (!hasPearlification) {
                pPlayer.addEffect(new MobEffectInstance(InitMobEffects.PEARLIFICATION.get(), 1200, 1));
            } else {
                pPlayer.addEffect(new MobEffectInstance(InitMobEffects.PEARLIFICATION.get(), pearlificationEffect.getDuration() + 1200, 1));
            }
        }

        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), InitSounds.EFFECT_CLEAR.get(), SoundSource.PLAYERS, 1F, 1F);

        if (!pPlayer.isCreative()) {
            pStack.hurtAndBreak(4, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
        }

        if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
            InitTriggers.ABSTRACT.trigger(pServerPlayer);
        }

        return InteractionResultHolder.success(pStack);
    }
}
