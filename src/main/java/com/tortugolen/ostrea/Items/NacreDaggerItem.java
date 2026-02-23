package com.tortugolen.ostrea.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NacreDaggerItem extends SwordItem {
    public NacreDaggerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (!Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.ostrea.shift").withStyle(ChatFormatting.YELLOW));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.ostrea.nacre_dagger_0").withStyle(ChatFormatting.YELLOW));
            pTooltipComponents.add(Component.translatable("tooltip.ostrea.nacre_dagger_1")
                    .append(Component.literal(" "))
                    .append(Component.translatable("item.ostrea.receptacle_pearl")));
        }
    }
}
