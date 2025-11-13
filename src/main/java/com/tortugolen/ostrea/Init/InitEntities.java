package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Entities.PearlTips.PearlTipProjectileEntity;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Ostrea.MOD_ID);

    public static final RegistryObject<EntityType<PearlTipProjectileEntity>> PEARL_TIP = ENTITY_TYPES.register(
                "pearl_tip", () -> EntityType.Builder.<PearlTipProjectileEntity>of(PearlTipProjectileEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(4)
                        .updateInterval(1)
                        .build("pearl_tip")
            );

}
