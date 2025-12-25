package com.tortugolen.ostrea.Events;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitMobEffects;
import com.tortugolen.ostrea.Init.InitSounds;
import com.tortugolen.ostrea.Items.PearlNecklaceItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PassiveAnnulmentEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player pPlayer = event.player;
        if (pPlayer.level().isClientSide) return;

        ItemStack necklaceStack = findValidNecklace(pPlayer);
        if (necklaceStack.isEmpty()) return;

        int level = necklaceStack.getEnchantmentLevel(InitEnchantments.PASSIVE_ANNULMENT.get());
        if (level <= 0) return;

        int intervalTicks = getIntervalTicks(level);
        if (intervalTicks < 0) return;

        if (intervalTicks > 0 && pPlayer.tickCount % intervalTicks != 0) return;

        var activeEffects = pPlayer.getActiveEffects().stream().toList();
        if (activeEffects.isEmpty()) return;

        boolean hasSelectiveBlessing =
                necklaceStack.getEnchantmentLevel(InitEnchantments.SELECTIVE_BLESSING.get()) > 0;

        var removableEffects = activeEffects.stream()
                .filter(e -> !hasSelectiveBlessing || !e.getEffect().isBeneficial())
                .filter(e -> e.getEffect() != InitMobEffects.PEARLIFICATION.get())
                .toList();

        if (removableEffects.isEmpty()) return;

        MobEffectInstance chosen =
                removableEffects.get(pPlayer.level().random.nextInt(removableEffects.size()));

        MobEffect effect = chosen.getEffect();
        int color = effect.getColor();

        pPlayer.removeEffect(effect);

        pPlayer.level().playSound(
                null,
                pPlayer.getX(),
                pPlayer.getY(),
                pPlayer.getZ(),
                InitSounds.EFFECT_CLEAR.get(),
                SoundSource.PLAYERS,
                1F,
                1F
        );

        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;

        for (int i = 0; i < 50; i++) {
            double ox = (pPlayer.level().random.nextDouble() - 0.5) * 2;
            double oy = (pPlayer.level().random.nextDouble() - 0.2) * 0.3;
            double oz = (pPlayer.level().random.nextDouble() - 0.5) * 2;

            pPlayer.level().addParticle(
                    ParticleTypes.ENTITY_EFFECT,
                    pPlayer.getX() + ox,
                    pPlayer.getY() + 1 + oy,
                    pPlayer.getZ() + oz,
                    r, g, b
            );
        }
    }

    private static ItemStack findValidNecklace(Player pPlayer) {
        ItemStack mainHand = pPlayer.getMainHandItem();
        ItemStack offHand = pPlayer.getOffhandItem();
        ItemStack helmet = pPlayer.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = pPlayer.getItemBySlot(EquipmentSlot.CHEST);

        if (isValid(mainHand)) return mainHand;
        if (isValid(offHand)) return offHand;
        if (isValid(helmet)) return helmet;
        if (isValid(chest)) return chest;

        for (ItemStack stack : pPlayer.getInventory().items) {
            if (isValid(stack) && stack.getEnchantmentLevel(InitEnchantments.PASSIVE_ANNULMENT.get()) >= 5) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    private static boolean isValid(ItemStack stack) {
        return !stack.isEmpty()
                && stack.getItem() instanceof PearlNecklaceItem
                && stack.getEnchantmentLevel(InitEnchantments.PASSIVE_ANNULMENT.get()) > 0;
    }

    private static int getIntervalTicks(int level) {
        return switch (level) {
            case 1 -> 60 * 20;
            case 2 -> 30 * 20;
            case 3 -> 15 * 20;
            case 4, 5 -> 0;
            default -> -1;
        };
    }
}

