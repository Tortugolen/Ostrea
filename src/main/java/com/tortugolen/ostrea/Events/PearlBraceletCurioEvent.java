package com.tortugolen.ostrea.Events;

import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitTriggers;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = Ostrea.MOD_ID)
public class PearlBraceletCurioEvent {

    @SubscribeEvent
    public static void onHit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player pPlayer)) return;

        CuriosApi.getCuriosInventory(pPlayer).ifPresent(inventory -> {
            inventory.findFirstCurio(InitItems.PEARL_BRACELET.get()).ifPresent(slotResult -> {
                ItemStack pStack = slotResult.stack();
                LivingEntity pTarget = event.getEntity();

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
                        pStack.hurtAndBreak(4, pPlayer, player -> {
                        });
                    }

                    if (pPlayer instanceof ServerPlayer pServerPlayer) {
                        InitTriggers.ABSTRACT0.trigger(pServerPlayer);
                    }

                    break;
                }
            });
        });
    }
}