package com.tortugolen.ostrea.Renderers.PearlTips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tortugolen.ostrea.Entities.PearlTips.IronPearlTipProjectileEntity;
import com.tortugolen.ostrea.Models.PearlTips.IronPearlTipProjectileModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class IronPearlTipProjectileRenderer extends EntityRenderer<IronPearlTipProjectileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("ostrea:textures/entity/iron_pearl_tip.png");
    private final IronPearlTipProjectileModel model;

    public IronPearlTipProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new IronPearlTipProjectileModel(context.bakeLayer(IronPearlTipProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(IronPearlTipProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.pushPose();
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(IronPearlTipProjectileEntity pEntity) {
        return TEXTURE;
    }}
