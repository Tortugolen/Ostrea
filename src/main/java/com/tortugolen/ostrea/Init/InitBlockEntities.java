package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.BlockEntities.*;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Ostrea.MOD_ID);

    public static final RegistryObject<BlockEntityType<OysterBlockEntity>> OYSTER_BE = BLOCK_ENTITIES.register(
            "oyster_be", () -> BlockEntityType.Builder.of(OysterBlockEntity::new, InitBlocks.OYSTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<MechanicalOysterBlockEntity>> MECHANICAL_OYSTER_BE = BLOCK_ENTITIES.register(
            "mechanical_oyster_be", () -> BlockEntityType.Builder.of(MechanicalOysterBlockEntity::new, InitBlocks.MECHANICAL_OYSTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<CrusherBlockEntity>> CRUSHER_BE = BLOCK_ENTITIES.register(
            "crusher_be", () -> BlockEntityType.Builder.of(CrusherBlockEntity::new, InitBlocks.CRUSHER.get()).build(null));
    public static final RegistryObject<BlockEntityType<NacreAltarBlockEntity>> NACRE_ALTAR_BE = BLOCK_ENTITIES.register(
            "nacre_altar_be", () -> BlockEntityType.Builder.of(NacreAltarBlockEntity::new, InitBlocks.NACRE_ALTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<DeepslateAltarBlockEntity>> DEEPSLATE_ALTAR_BE = BLOCK_ENTITIES.register(
            "deepslate_altar_be", () -> BlockEntityType.Builder.of(DeepslateAltarBlockEntity::new, InitBlocks.DEEPSLATE_ALTAR.get()).build(null));

}
