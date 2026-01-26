package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceptaclePearlItem extends Item {
    private static final String EFFECT_STORED = "stored_effect";
    private static final String EFFECT_AMOUNT = "effect_amount";
    private final int MAX_AMOUNT = 64;

    public static final Map<EntityType<?>, MobEffect> ENTITY_EFFECTS = new HashMap<>();

    static {
        ENTITY_EFFECTS.put(EntityType.HUSK, MobEffects.HUNGER);
        ENTITY_EFFECTS.put(EntityType.WITHER_SKELETON, MobEffects.WITHER);
        ENTITY_EFFECTS.put(EntityType.WITHER, MobEffects.WITHER);
        ENTITY_EFFECTS.put(EntityType.STRAY, MobEffects.MOVEMENT_SLOWDOWN);
        ENTITY_EFFECTS.put(EntityType.DOLPHIN, MobEffects.DOLPHINS_GRACE);
        ENTITY_EFFECTS.put(EntityType.CAVE_SPIDER, MobEffects.POISON);
        ENTITY_EFFECTS.put(EntityType.PUFFERFISH, MobEffects.POISON);
        ENTITY_EFFECTS.put(EntityType.BEE, MobEffects.POISON);
        ENTITY_EFFECTS.put(EntityType.AXOLOTL, MobEffects.REGENERATION);
        ENTITY_EFFECTS.put(EntityType.WARDEN, MobEffects.DARKNESS);
        //ENTITY_EFFECTS.put(EntityType.TURTLE, MobEffects.DAMAGE_RESISTANCE);
        //ENTITY_EFFECTS.put(EntityType.TURTLE, MobEffects.MOVEMENT_SLOWDOWN);
    }

    public ReceptaclePearlItem(Properties pProperties) {
        super(pProperties);
    }

    public void storeEffect(ItemStack pStack, MobEffect pEffect, LivingEntity pAttacker) {
        CompoundTag tag = pStack.getOrCreateTag();
        ItemStack mainHandItem = pAttacker.getMainHandItem();
        Level pLevel = pAttacker.level();
        int amount = tag.getInt(EFFECT_AMOUNT);
        int value = (mainHandItem.getItem() == InitItems.NACRE_DAGGER.get()) ? 4 : 1;

        if (tag.contains(EFFECT_STORED)) {
            if (amount < MAX_AMOUNT) {
                if (amount + value <= MAX_AMOUNT) {
                    tag.putInt(EFFECT_AMOUNT, amount + value);
                } else {
                    tag.putInt(EFFECT_AMOUNT, MAX_AMOUNT);
                }
            }

            if (amount + value >= MAX_AMOUNT) {
                if (pLevel instanceof ServerLevel pServerLevel && pAttacker instanceof ServerPlayer pServerPlayer) {
                    InitTriggers.ABSTRACT3.trigger(pServerPlayer);
                }
            }

            return;
        }

        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(pEffect);

        if (id == null) return;
        if (amount > MAX_AMOUNT) tag.putInt(EFFECT_AMOUNT, MAX_AMOUNT);

        tag.putString(EFFECT_STORED, id.toString());
        tag.putInt(EFFECT_AMOUNT, value);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.isAlive()) {
            MobEffect pEffect = ENTITY_EFFECTS.get(pTarget.getType());

            if (pEffect != null) {
                storeEffect(pStack, pEffect, pAttacker);
            }
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getTag();

        if (tag == null) return;

        int amount = tag.getInt(EFFECT_AMOUNT);
        String effectId = tag.getString(EFFECT_STORED);
        MobEffect pEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));

        if (pEffect == null) return;

        pTooltipComponents.add(Component.empty().append(pEffect.getDisplayName().copy().withStyle(style -> style.withColor(pEffect.getColor()))).append(" ").append(Component.literal(String.valueOf(amount))).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();

        return tag != null && tag.contains(EFFECT_STORED);
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();

        if (tag == null) return 0;

        int amount = tag.getInt(EFFECT_AMOUNT);

        return Math.round(13.0F * amount / MAX_AMOUNT);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();

        if (tag == null) return 0xFFFFFF;

        String effectId = tag.getString(EFFECT_STORED);
        MobEffect pEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));

        return pEffect != null ? pEffect.getColor() : 0xFFFFFF;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();

        if (tag == null) return false;

        int amount = tag.getInt(EFFECT_AMOUNT);

        return amount == MAX_AMOUNT;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }
}
