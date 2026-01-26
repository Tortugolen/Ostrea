package com.tortugolen.ostrea;

import com.tortugolen.ostrea.Init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Ostrea.MOD_ID)
public class Ostrea {
    public static final String MOD_ID = "ostrea";
    public Ostrea() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitTabs.TABS.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);
        InitBlocks.BLOCKS.register(modEventBus);
        InitSounds.SOUND_EVENTS.register(modEventBus);
        InitEntities.ENTITY_TYPES.register(modEventBus);
        InitBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        InitMenus.MENUS.register(modEventBus);
        InitRecipes.SERIALIZERS.register(modEventBus);
        InitRecipeTypes.RECIPE_TYPES.register(modEventBus);
        InitEnchantments.ENCHANTMENTS.register(modEventBus);
        InitMobEffects.MOB_EFFECTS.register(modEventBus);
        InitFeatures.FEATURES.register(modEventBus);
        InitPois.POI_TYPES.register(modEventBus);
        InitProfessions.PROFESSIONS.register(modEventBus);
        InitLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        InitTriggers.register();
    }
}
