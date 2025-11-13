package com.tortugolen.ostrea.Items.PearlTips;

import com.tortugolen.ostrea.Entities.PearlTips.GoldPearlTipProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GoldPearlTipItem extends Item {
    public GoldPearlTipItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.playSound(SoundEvents.SNOWBALL_THROW, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (pLevel instanceof ServerLevel serverLevel) {
            GoldPearlTipProjectileEntity pearlTipProjectile = new GoldPearlTipProjectileEntity(pPlayer, pLevel);
            pearlTipProjectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2F, 0F);
            pLevel.addFreshEntity(pearlTipProjectile);
        }

        if (!pPlayer.isCreative()) {
            itemstack.shrink(1);
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        pPlayer.getCooldowns().addCooldown(this, 10);

        return InteractionResultHolder.success(itemstack);
    }
}
