package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class PearlBraceletItem extends Item {

    public PearlBraceletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        InteractionHand pUsedHand = pAttacker.getUsedItemHand();
        Level pLevel = pAttacker.level();

        if (!(pAttacker instanceof Player pPlayer)) {
            return super.hurtEnemy(pStack, pTarget, pAttacker);
        }

        if (pPlayer.getActiveEffects().isEmpty()) {
            return super.hurtEnemy(pStack, pTarget, pAttacker);
        }

        boolean hasSelectiveBlessing = EnchantmentHelper.getItemEnchantmentLevel(InitEnchantments.SELECTIVE_BLESSING.get(), pStack) > 0;

        for (MobEffectInstance effect : pPlayer.getActiveEffects()) {

            if (hasSelectiveBlessing && effect.getEffect().isBeneficial()) {
                continue;
            }

            pTarget.addEffect(new MobEffectInstance(
                    effect.getEffect(),
                    effect.getDuration(),
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            ));

            pPlayer.removeEffect(effect.getEffect());

            if (!pPlayer.isCreative()) {
                pStack.hurtAndBreak(4, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
            }

            if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                InitTriggers.ABSTRACT0.trigger(pServerPlayer);
            }

            break;
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }
}
