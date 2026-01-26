package com.tortugolen.ostrea.Base;

import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.model.HumanoidModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ostrea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeRenderers {
    @SubscribeEvent
    public static void onPlayerRenderPost(RenderPlayerEvent.Post event) {
        HumanoidModel<?> model = event.getRenderer().getModel();

        model.head.visible = true;
        model.hat.visible = true;
    }
}
