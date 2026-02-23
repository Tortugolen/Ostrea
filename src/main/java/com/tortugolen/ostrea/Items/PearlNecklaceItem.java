package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitMobEffects;
import com.tortugolen.ostrea.Init.InitSounds;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Map;

public class PearlNecklaceItem extends Item implements ICurioItem {
    private int PARTICLE_NUMBER = 50;
    private int HORIZONTAL_DISPERSION = 2;
    private double VERTICAL_DISPERSION = 0.3;
    private int INITIAL_POSITION = 1;

    public PearlNecklaceItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        var activeEffectsList = pPlayer.getActiveEffects().stream().toList();
        if (activeEffectsList.isEmpty()) return InteractionResultHolder.fail(pStack);

        var harmfulEffectsList = activeEffectsList.stream()
                .filter(e -> !e.getEffect().isBeneficial())
                .filter(e -> e.getEffect() != InitMobEffects.PEARLIFICATION.get())
                .toList();

        Map<Enchantment, Integer> enchantments = pStack.getAllEnchantments();
        boolean hasSelectiveBlessing = enchantments.containsKey(InitEnchantments.SELECTIVE_BLESSING.get());

        MobEffectInstance selectedEffectInstance;

        if (hasSelectiveBlessing) {
            if (harmfulEffectsList.isEmpty()) {
                return InteractionResultHolder.fail(pStack);
            }
            selectedEffectInstance = harmfulEffectsList.get(pLevel.random.nextInt(harmfulEffectsList.size()));
        } else {
            selectedEffectInstance = activeEffectsList.get(pLevel.random.nextInt(activeEffectsList.size()));
        }

        MobEffect effect = selectedEffectInstance.getEffect();
        int color = effect.getColor();

        boolean removed;

        removed = pPlayer.removeEffect(effect);

        if (!removed) return InteractionResultHolder.fail(pStack);

        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), InitSounds.EFFECT_CLEAR.get(), SoundSource.PLAYERS, 1F, 1F);

        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;

        for (int i = 0; i < PARTICLE_NUMBER; i++) {
            double ox = (pLevel.random.nextDouble() - 0.5) * HORIZONTAL_DISPERSION;
            double oy = (pLevel.random.nextDouble() - 0.2) * VERTICAL_DISPERSION;
            double oz = (pLevel.random.nextDouble() - 0.5) * HORIZONTAL_DISPERSION;

            pLevel.addParticle(ParticleTypes.ENTITY_EFFECT, pPlayer.getX() + ox, pPlayer.getY() + INITIAL_POSITION + oy, pPlayer.getZ() + oz, r, g, b);
        }

        if (!pPlayer.isCreative()) {
            pStack.hurtAndBreak(4, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
        }

        if (pPlayer instanceof ServerPlayer pServerPlayer) {
            InitTriggers.ABSTRACT5.trigger(pServerPlayer);
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
            boolean isValidEnchantment = enchantment.category == InitEnchantments.PEARL_NECKLACE_CATEGORY || enchantment.category == InitEnchantments.PEARL_JEWEL_CATEGORY || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING;
            if (!isValidEnchantment) return false;
        }
        return true;
    }

    @Override
    public void curioTick(SlotContext pContext, ItemStack pStack) {
        LivingEntity pEntity = pContext.entity();
        Level pLevel = pEntity.level();

        if (pLevel.isClientSide) return;
        if (pEntity.tickCount % 20 != 0) return;

        var activeEffectsList = pEntity.getActiveEffects().stream().toList();
        if (activeEffectsList.isEmpty()) return;

        var harmfulEffectsList = activeEffectsList.stream()
                .filter(e -> !e.getEffect().isBeneficial())
                .filter(e -> e.getEffect() != InitMobEffects.PEARLIFICATION.get())
                .toList();

        Map<Enchantment, Integer> enchantments = pStack.getAllEnchantments();
        boolean hasSelectiveBlessing = enchantments.containsKey(InitEnchantments.SELECTIVE_BLESSING.get());
        boolean hasEffects = !pEntity.getActiveEffects().isEmpty();
        boolean hasHarmfulEffects = pEntity.getActiveEffects().stream().anyMatch(effect -> !effect.getEffect().isBeneficial());

        MobEffectInstance selectedEffectInstance = null;

        if (!hasSelectiveBlessing && hasEffects) {
            selectedEffectInstance = activeEffectsList.get(pLevel.random.nextInt(activeEffectsList.size()));
        }

        if (hasSelectiveBlessing && hasHarmfulEffects) {
            selectedEffectInstance = harmfulEffectsList.get(pLevel.random.nextInt(harmfulEffectsList.size()));
        }

        if (selectedEffectInstance != null) {
            MobEffect effect = selectedEffectInstance.getEffect();
            int color = effect.getColor();
            boolean removed = pEntity.removeEffect(effect);

            pEntity.removeEffect(selectedEffectInstance.getEffect());
            if (!removed) return;

            pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), InitSounds.EFFECT_CLEAR.get(), SoundSource.PLAYERS, 1F, 1F);

            if (pEntity instanceof Player pPlayer) {
                float r = ((color >> 16) & 0xFF) / 255F;
                float g = ((color >> 8) & 0xFF) / 255F;
                float b = (color & 0xFF) / 255F;

                for (int i = 0; i < PARTICLE_NUMBER; i++) {
                    double ox = (pLevel.random.nextDouble() - 0.5) * HORIZONTAL_DISPERSION;
                    double oy = (pLevel.random.nextDouble() - 0.2) * VERTICAL_DISPERSION;
                    double oz = (pLevel.random.nextDouble() - 0.5) * HORIZONTAL_DISPERSION;
                    pLevel.addParticle(ParticleTypes.ENTITY_EFFECT, pPlayer.getX() + ox, pPlayer.getY() + INITIAL_POSITION + oy, pPlayer.getZ() + oz, r, g, b);
                }
            }

            if (pEntity instanceof Player pPlayer && !pPlayer.isCreative()) {
                pStack.hurtAndBreak(4, pEntity, entity -> {});
            }

            if (pEntity instanceof ServerPlayer pServerPlayer) {
                InitTriggers.ABSTRACT5.trigger(pServerPlayer);
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
