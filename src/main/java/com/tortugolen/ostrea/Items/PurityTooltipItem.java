package com.tortugolen.ostrea.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PurityTooltipItem extends TooltipItem {
    public PurityTooltipItem(Properties pProperties, String tooltip) {
        super(pProperties, tooltip);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.ostrea.purity").append(Component.literal("§e: §r" + tooltip + "§e%§r")));
    }
}
