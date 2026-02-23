package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class PearlBraceletItem extends Item implements ICurioItem {

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
    public boolean canApplyAtEnchantingTable(ItemStack pStack, Enchantment pEnchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack pStack, ItemStack pBook) {
        ListTag enchantments = EnchantedBookItem.getEnchantments(pBook);
        for (int i = 0; i < enchantments.size(); i++) {
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.getCompound(i).getString("id")));
            if (enchantment == null) continue;
            boolean isValidEnchantment = enchantment.category == InitEnchantments.PEARL_JEWEL_CATEGORY || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING;
            if (!isValidEnchantment) return false;
        }
        return true;
    }

    @Override
    public void curioBreak(SlotContext pContext, ItemStack pStack) {
        LivingEntity pEntity = pContext.entity();
        Level pLevel = pEntity.level();

        pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1F);
    }
}
