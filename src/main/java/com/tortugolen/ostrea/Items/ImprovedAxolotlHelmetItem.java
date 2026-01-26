package com.tortugolen.ostrea.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImprovedAxolotlHelmetItem extends ArmorItem {
    public ImprovedAxolotlHelmetItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack pSstack, Level pLevel, Player pPlayer) {
        if (pPlayer.getHealth() < 6) {
            if (!pPlayer.hasEffect(MobEffects.REGENERATION)) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 320, 0));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        MobEffect pEffect = MobEffects.REGENERATION;

        if (!Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.ostrea.shift").withStyle(ChatFormatting.YELLOW));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.ostrea.improved_axolotl_helmet"));
            pTooltipComponents.add(Component.empty()
                    .append(pEffect.getDisplayName().copy().withStyle(style -> style.withColor(pEffect.getColor()))));
        }
    }
}