package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Base.NBTIngredient;
import com.tortugolen.ostrea.Models.AxolotlHelmetModel;
import com.tortugolen.ostrea.GUIs.Screens.CrusherScreen;
import com.tortugolen.ostrea.GUIs.Screens.MechanicalOysterScreen;
import com.tortugolen.ostrea.GUIs.Screens.OysterScreen;
import com.tortugolen.ostrea.Models.ImprovedAxolotlHelmetModel;
import com.tortugolen.ostrea.Models.PearlTips.CopperPearlTipProjectileModel;
import com.tortugolen.ostrea.Models.PearlTips.GoldPearlTipProjectileModel;
import com.tortugolen.ostrea.Models.PearlTips.IronPearlTipProjectileModel;
import com.tortugolen.ostrea.Models.PearlTips.PearlTipProjectileModel;
import com.tortugolen.ostrea.Ostrea;
import com.tortugolen.ostrea.Renderers.AxolotlHelmetRenderer;
import com.tortugolen.ostrea.Renderers.DeepslateAltarRenderer;
import com.tortugolen.ostrea.Renderers.ImprovedAxolotlHelmetRenderer;
import com.tortugolen.ostrea.Renderers.NacreAltarRenderer;
import com.tortugolen.ostrea.Renderers.PearlTips.CopperPearlTipProjectileRenderer;
import com.tortugolen.ostrea.Renderers.PearlTips.GoldPearlTipProjectileRenderer;
import com.tortugolen.ostrea.Renderers.PearlTips.IronPearlTipProjectileRenderer;
import com.tortugolen.ostrea.Renderers.PearlTips.PearlTipProjectileRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Ostrea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitRenders {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(InitEntities.PEARL_TIP.get(), PearlTipProjectileRenderer::new);
        event.registerEntityRenderer(InitEntities.IRON_PEARL_TIP.get(), IronPearlTipProjectileRenderer::new);
        event.registerEntityRenderer(InitEntities.COPPER_PEARL_TIP.get(), CopperPearlTipProjectileRenderer::new);
        event.registerEntityRenderer(InitEntities.GOLD_PEARL_TIP.get(), GoldPearlTipProjectileRenderer::new);

        event.registerBlockEntityRenderer(InitBlockEntities.NACRE_ALTAR_BE.get(), NacreAltarRenderer::new);
        event.registerBlockEntityRenderer(InitBlockEntities.DEEPSLATE_ALTAR_BE.get(), DeepslateAltarRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.ARAGONITE_SHARD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.ARAGONITE_CLUSTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.CRUSHER.get(), RenderType.cutout());

        MenuScreens.register(InitMenus.OYSTER_MENU.get(), OysterScreen::new);
        MenuScreens.register(InitMenus.MECHANICAL_OYSTER_MENU.get(), MechanicalOysterScreen::new);
        MenuScreens.register(InitMenus.CRUSHER_MENU.get(), CrusherScreen::new);

        event.enqueueWork(() -> {
            CraftingHelper.register(new ResourceLocation(Ostrea.MOD_ID, "nbt_ingredient"), NBTIngredient.Serializer.INSTANCE);
        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PearlTipProjectileModel.LAYER_LOCATION, PearlTipProjectileModel::createBodyLayer);
        event.registerLayerDefinition(IronPearlTipProjectileModel.LAYER_LOCATION, IronPearlTipProjectileModel::createBodyLayer);
        event.registerLayerDefinition(CopperPearlTipProjectileModel.LAYER_LOCATION, CopperPearlTipProjectileModel::createBodyLayer);
        event.registerLayerDefinition(GoldPearlTipProjectileModel.LAYER_LOCATION, GoldPearlTipProjectileModel::createBodyLayer);

        event.registerLayerDefinition(AxolotlHelmetModel.LAYER_LOCATION, AxolotlHelmetModel::createBodyLayer);
        event.registerLayerDefinition(ImprovedAxolotlHelmetModel.LAYER_LOCATION, ImprovedAxolotlHelmetModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skin -> {
            LivingEntityRenderer<Player, HumanoidModel<Player>> renderer = event.getSkin(skin);

            if (renderer != null) {
                renderer.addLayer(new AxolotlHelmetRenderer(renderer));
                renderer.addLayer(new ImprovedAxolotlHelmetRenderer(renderer));
            }
        });
    }
}
