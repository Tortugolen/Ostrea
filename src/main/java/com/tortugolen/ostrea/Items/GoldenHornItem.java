package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

public class GoldenHornItem extends Item {
    private final ResourceLocation instrumentId;

    public GoldenHornItem(Item.Properties pProperties, ResourceLocation pInstrumentId) {
        super(pProperties);
        this.instrumentId = pInstrumentId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        Optional<? extends Holder<Instrument>> optional = this.getInstrument();

        if (optional.isPresent()) {
            Instrument instrument = optional.get().value();
            pPlayer.startUsingItem(pUsedHand);
            play(pLevel, pPlayer, instrument);
            pPlayer.getCooldowns().addCooldown(this, instrument.useDuration());
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            if (hasNearWater(pPlayer)) {
                pPlayer.addEffect(new MobEffectInstance(InitMobEffects.ABYSSMUS_RAGE.get(), 1200, 0));
            }
            return InteractionResultHolder.consume(itemstack);
        }

        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return this.getInstrument().map(holder -> holder.value().useDuration()).orElse(0);
    }

    private Optional<? extends Holder<Instrument>> getInstrument() {
        return BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, this.instrumentId));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }

    private static void play(Level pLevel, Player pPlayer, Instrument pInstrument) {
        SoundEvent soundevent = pInstrument.soundEvent().value();
        float f = pInstrument.range() / 16.0F;
        pLevel.playSound(pPlayer, pPlayer, soundevent, SoundSource.RECORDS, f, 1.0F);
        pLevel.gameEvent(GameEvent.INSTRUMENT_PLAY, pPlayer.position(), GameEvent.Context.of(pPlayer));
    }

    private boolean hasNearWater(Player pPlayer) {
        BlockPos pPos = pPlayer.blockPosition();
        Level pLevel = pPlayer.level();

        if (pLevel.isRaining()) return true;
        if (pLevel.isThundering()) return true;

        for (BlockPos waterPos : BlockPos.betweenClosed(pPos.offset(-4, -4, -4), pPos.offset(4, 4, 4))) {
            BlockState pState = pPlayer.level().getBlockState(waterPos);
            if (pState.is(Blocks.WATER) || pState.getFluidState().is(Fluids.WATER)) {
                return true;
            }
        }

        return false;
    }
}