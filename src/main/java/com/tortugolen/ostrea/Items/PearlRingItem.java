package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitMobEffects;
import com.tortugolen.ostrea.Init.InitSounds;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Map;

public class PearlRingItem extends Item implements ICurioItem {
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

        if (pPlayer instanceof ServerPlayer pServerPlayer) {
            InitTriggers.ABSTRACT1.trigger(pServerPlayer);
        }

        return InteractionResultHolder.success(pStack);
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
    public void curioTick(SlotContext pContext, ItemStack pStack) {
        LivingEntity pEntity = pContext.entity();
        Level pLevel = pEntity.level();

        Map<Enchantment, Integer> enchantments = pStack.getAllEnchantments();

        MobEffectInstance pearlificationEffect = pEntity.getEffect(InitMobEffects.PEARLIFICATION.get());

        boolean hasSelectiveBlessing = enchantments.containsKey(InitEnchantments.SELECTIVE_BLESSING.get());
        boolean hasEffects = !pEntity.getActiveEffects().isEmpty();
        boolean hasHarmfulEffects = pEntity.getActiveEffects().stream().anyMatch(effect -> !effect.getEffect().isBeneficial());
        boolean hasPearlification = pearlificationEffect != null;

        if (!hasPearlification) {
            if (!hasSelectiveBlessing && hasEffects) {
                pEntity.addEffect(new MobEffectInstance(InitMobEffects.PEARLIFICATION.get(), 1200, 0));
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), InitSounds.EFFECT_CLEAR.get(), SoundSource.PLAYERS, 1F, 1F);

                if (pEntity instanceof Player pPlayer && !pPlayer.isCreative()) {
                    pStack.hurtAndBreak(4, pEntity, entity -> {
                    });
                }

                if (pLevel instanceof ServerLevel pServerLevel && pEntity instanceof ServerPlayer pServerPlayer) {
                    InitTriggers.ABSTRACT1.trigger(pServerPlayer);
                }
            }

            if (hasSelectiveBlessing && hasHarmfulEffects) {
                pEntity.addEffect(new MobEffectInstance(InitMobEffects.PEARLIFICATION.get(), 1200, 1));
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), InitSounds.EFFECT_CLEAR.get(), SoundSource.PLAYERS, 1F, 1F);

                if (pEntity instanceof Player pPlayer && !pPlayer.isCreative()) {
                    pStack.hurtAndBreak(4, pEntity, entity -> {
                    });
                }

                if (pEntity instanceof ServerPlayer pServerPlayer) {
                    InitTriggers.ABSTRACT1.trigger(pServerPlayer);
                }
            }
        }
    }

    @Override
    public void curioBreak(SlotContext pContext, ItemStack pStack) {
        LivingEntity pEntity = pContext.entity();
        Level pLevel = pEntity.level();

        pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1F);
    }
}
