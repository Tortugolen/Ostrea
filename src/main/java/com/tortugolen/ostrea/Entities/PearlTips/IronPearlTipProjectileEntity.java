package com.tortugolen.ostrea.Entities.PearlTips;

import com.tortugolen.ostrea.Init.InitEntities;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class IronPearlTipProjectileEntity extends ThrowableItemProjectile {
    float DAMAGE = 6F;
    float RECYCLE = 0.75F;

    public IronPearlTipProjectileEntity(EntityType<? extends IronPearlTipProjectileEntity> type, Level level) {
        super(type, level);
    }

    public IronPearlTipProjectileEntity(LivingEntity shooter, Level level) {
        super(InitEntities.IRON_PEARL_TIP.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return InitItems.IRON_PEARL_TIP.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level() instanceof ServerLevel pLevel) {
            if (result.getEntity() instanceof LivingEntity pEntity) {
                BlockPos pPosition = pEntity.blockPosition();
                DamageSource source = this.damageSources().thrown(this, this.getOwner());
                pEntity.hurt(source, DAMAGE);
                pLevel.playSound(null, pPosition, InitSounds.PEARL_TTP_BREAK.get(), SoundSource.PLAYERS, 16F, 1F);
            }
            spawnBreakParticles();
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.level() instanceof ServerLevel pLevel) {
            BlockPos pPosition = result.getBlockPos();
            if (this.random.nextFloat() < RECYCLE) {
                pLevel.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(pLevel, pPosition.getX() + 0.5F, pPosition.getY() + 1F, pPosition.getZ() + 0.5F, this.getItem()));
            } else {
                pLevel.playSound(null, pPosition, InitSounds.PEARL_TTP_BREAK.get(), SoundSource.BLOCKS, 16F, 1F);
            }
            spawnBreakParticles();
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel serverLevel && result.getType() == HitResult.Type.MISS) {
            this.discard();
        }
    }

    private ParticleOptions getParticle() {
        ItemStack stack = this.getItem();
        if (stack.isEmpty()) {
            return ParticleTypes.ITEM_SNOWBALL;
        }
        return new ItemParticleOption(ParticleTypes.ITEM, stack);
    }

    private void spawnBreakParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleOptions particle = getParticle();
            serverLevel.sendParticles(particle, this.getX(), this.getY(), this.getZ(), 8, 0.2, 0.2, 0.2, 0.05);
        }

        if (this.level().isClientSide) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(getParticle(), this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5) * 0.2, (this.random.nextDouble() - 0.5) * 0.2, (this.random.nextDouble() - 0.5) * 0.2);
            }
        }
    }
}
