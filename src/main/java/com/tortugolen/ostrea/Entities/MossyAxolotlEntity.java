package com.tortugolen.ostrea.Entities;

import com.tortugolen.ostrea.Init.InitEntities;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MossyAxolotlEntity extends Axolotl {
    public MossyAxolotlEntity(EntityType<? extends Axolotl> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor pLevel, MobSpawnType pSpawnReason) {
        return pLevel.getEntitiesOfClass(MossyAxolotlEntity.class, this.getBoundingBox().inflate(256.0D)).isEmpty();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
        this.setVariant(Axolotl.Variant.LUCY);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Nullable
    @Override
    public Axolotl getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        Axolotl baby = EntityType.AXOLOTL.create(pLevel);
        if (baby != null) {
            baby.setVariant(Axolotl.Variant.LUCY);
        }
        return baby;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (pStack.is(Items.WATER_BUCKET) && this.isAlive()) {
            this.playSound(this.getPickupSound(), 1.0F, 1.0F);
            ItemStack filledBucket = this.getBucketItemStack();
            this.saveToBucketTag(filledBucket);

            if (!pPlayer.getAbilities().instabuild) {
                pStack.shrink(1);
            }

            if (pStack.isEmpty()) {
                pPlayer.setItemInHand(pUsedHand, filledBucket);
            } else if (!pPlayer.getInventory().add(filledBucket)) {
                pPlayer.drop(filledBucket, false);
            }

            this.discard();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (pStack.is(Items.SHEARS) && this.isAlive()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, pPlayer);
            if (!pPlayer.isCreative()) {
                pStack.hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(pPlayer, pUsedHand);
    }

    @Override
    public void saveToBucketTag(ItemStack pStack) {
        super.saveToBucketTag(pStack);
        CompoundTag tag = pStack.getOrCreateTag();
        tag.putString("axolotl", "true");
    }

    @Override
    public void loadFromBucketTag(CompoundTag pTag) {
        super.loadFromBucketTag(pTag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(InitItems.MOSSY_AXOLOTL_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    private void shear(SoundSource pCategory) {
        shearInternal(pCategory).forEach(s -> this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(1.0D), this.getZ(), s)));
    }

    private List<ItemStack> shearInternal(SoundSource pCategory) {
        this.level().playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, pCategory, 1.0F, 1.0F);
        if (!this.level().isClientSide()) {
            Axolotl axolotl = EntityType.AXOLOTL.create(this.level());
            if (axolotl != null) {
                ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.25D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                this.discard();
                axolotl.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                axolotl.setHealth(this.getHealth());
                axolotl.setVariant(Axolotl.Variant.LUCY);
                axolotl.yBodyRot = this.yBodyRot;
                if (this.hasCustomName()) {
                    axolotl.setCustomName(this.getCustomName());
                    axolotl.setCustomNameVisible(this.isCustomNameVisible());
                }

                if (this.isPersistenceRequired()) {
                    axolotl.setPersistenceRequired();
                }

                axolotl.setInvulnerable(this.isInvulnerable());
                this.level().addFreshEntity(axolotl);

                List<ItemStack> items = new ArrayList<>();
                items.add(new ItemStack(Items.MOSS_CARPET));
                return items;
            }
        }
        return Collections.emptyList();

    }

    @Override
    public void die(DamageSource pDamageSource) {
        if (isPlayingDead() && pDamageSource.getEntity() instanceof Player pPlayer && pPlayer.hasEffect(InitMobEffects.ABYSSMUS_RAGE.get())) {
            Level pLevel = pPlayer.level();

            if (!pLevel.isClientSide) {
                AncestralAxolotlEntity ancestralAxolotl = InitEntities.ANCESTRAL_AXOLOTL.get().create(pLevel);

                if (ancestralAxolotl != null) {
                    ancestralAxolotl.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                    pLevel.addFreshEntity(ancestralAxolotl);
                    ((ServerLevel) pLevel).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.5, this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                    pLevel.playSound(null, this.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F);
                }
            }

            pPlayer.removeEffect(InitMobEffects.ABYSSMUS_RAGE.get());
            this.discard();
            return;
        }

        super.die(pDamageSource);
    }
}