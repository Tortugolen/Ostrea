package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Entities.AncestralAxolotlEntity;
import com.tortugolen.ostrea.Entities.MossyAxolotlEntity;
import com.tortugolen.ostrea.Entities.PearlTips.CopperPearlTipProjectileEntity;
import com.tortugolen.ostrea.Entities.PearlTips.GoldPearlTipProjectileEntity;
import com.tortugolen.ostrea.Entities.PearlTips.IronPearlTipProjectileEntity;
import com.tortugolen.ostrea.Entities.PearlTips.PearlTipProjectileEntity;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Ostrea.MOD_ID);

    public static final RegistryObject<EntityType<MossyAxolotlEntity>> MOSSY_AXOLOTL = ENTITY_TYPES.register(
            "mossy_axolotl", () -> EntityType.Builder.of(MossyAxolotlEntity::new, MobCategory.AXOLOTLS)
                    .sized(0.75F, 0.42F)
                    .clientTrackingRange(10)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(3)
                    .build("mossy_axolotl"));

    public static final RegistryObject<EntityType<AncestralAxolotlEntity>> ANCESTRAL_AXOLOTL = ENTITY_TYPES.register(
            "ancestral_axolotl", () -> EntityType.Builder.of(AncestralAxolotlEntity::new, MobCategory.MONSTER)
                    .sized(0.75F, 0.42F)
                    .clientTrackingRange(10)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(3)
                    .build("ancestral_axolotl"));

    //No Mobs

    public static final RegistryObject<EntityType<PearlTipProjectileEntity>> PEARL_TIP = ENTITY_TYPES.register(
            "pearl_tip", () -> EntityType.Builder.<PearlTipProjectileEntity>of(PearlTipProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("pearl_tip")
    );
    public static final RegistryObject<EntityType<IronPearlTipProjectileEntity>> IRON_PEARL_TIP = ENTITY_TYPES.register(
            "iron_pearl_tip", () -> EntityType.Builder.<IronPearlTipProjectileEntity>of(IronPearlTipProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("iron_pearl_tip")
    );
    public static final RegistryObject<EntityType<CopperPearlTipProjectileEntity>> COPPER_PEARL_TIP = ENTITY_TYPES.register(
            "copper_pearl_tip", () -> EntityType.Builder.<CopperPearlTipProjectileEntity>of(CopperPearlTipProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("copper_pearl_tip")
    );
    public static final RegistryObject<EntityType<GoldPearlTipProjectileEntity>> GOLD_PEARL_TIP = ENTITY_TYPES.register(
            "gold_pearl_tip", () -> EntityType.Builder.<GoldPearlTipProjectileEntity>of(GoldPearlTipProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("gold_pearl_tip")
    );
}
