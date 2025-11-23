package com.tortugolen.ostrea.Renderers.PearlTips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tortugolen.ostrea.Entities.PearlTips.CopperPearlTipProjectileEntity;
import com.tortugolen.ostrea.Models.PearlTips.CopperPearlTipProjectileModel;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CopperPearlTipProjectileRenderer extends EntityRenderer<CopperPearlTipProjectileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("ostrea:textures/entity/copper_pearl_tip.png");
    private final CopperPearlTipProjectileModel model;

    public CopperPearlTipProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CopperPearlTipProjectileModel<>(context.bakeLayer(CopperPearlTipProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(CopperPearlTipProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.pushPose();
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperPearlTipProjectileEntity pEntity) {
        return TEXTURE;
    }
}
