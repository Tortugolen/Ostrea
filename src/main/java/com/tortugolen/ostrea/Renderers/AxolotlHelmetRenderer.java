package com.tortugolen.ostrea.Renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tortugolen.ostrea.Items.AxolotlHelmetItem;
import com.tortugolen.ostrea.Models.AxolotlHelmetModel;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AxolotlHelmetRenderer extends RenderLayer<Player, HumanoidModel<Player>> {

    private final AxolotlHelmetModel model;

    public AxolotlHelmetRenderer(RenderLayerParent<Player, HumanoidModel<Player>> parent) {
        super(parent);
        this.model = new AxolotlHelmetModel(Minecraft.getInstance().getEntityModels().bakeLayer(AxolotlHelmetModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pPose, MultiBufferSource pBuffer, int light, Player pPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack pStack = pPlayer.getItemBySlot(EquipmentSlot.HEAD);

        if (!(pStack.getItem() instanceof AxolotlHelmetItem)) return;

        pPose.pushPose();

        this.getParentModel().head.copyFrom(this.model.head);

        this.model.setupAnim(pPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ResourceLocation texture = new ResourceLocation(Ostrea.MOD_ID, "textures/armor/axolotl_helmet.png");

        VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(texture));

        this.model.renderToBuffer(pPose, consumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        pPose.popPose();
    }
}