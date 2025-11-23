package com.tortugolen.ostrea.Renderers.PearlTips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tortugolen.ostrea.Entities.PearlTips.GoldPearlTipProjectileEntity;
import com.tortugolen.ostrea.Models.PearlTips.GoldPearlTipProjectileModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GoldPearlTipProjectileRenderer extends EntityRenderer<GoldPearlTipProjectileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("ostrea:textures/entity/gold_pearl_tip.png");
    private final GoldPearlTipProjectileModel model;

    public GoldPearlTipProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GoldPearlTipProjectileModel<>(context.bakeLayer(GoldPearlTipProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(GoldPearlTipProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.pushPose();
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldPearlTipProjectileEntity pEntity) {
        return TEXTURE;
    }
}
