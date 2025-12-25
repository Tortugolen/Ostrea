package com.tortugolen.ostrea.Renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tortugolen.ostrea.BlockEntities.DeepslateAltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class DeepslateAltarRenderer implements BlockEntityRenderer<DeepslateAltarBlockEntity> {

    public DeepslateAltarRenderer(BlockEntityRendererProvider.Context pContext) {}

    @Override
    public void render(DeepslateAltarBlockEntity pBlockEntity, float partialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack pStack = pBlockEntity.getItem();
        if (pStack == null || pStack.isEmpty()) {
            return;
        }
        if (!pStack.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5, 1.375, 0.5);
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
            long gameTime = pBlockEntity.getLevel().getGameTime();
            float rotationAngle = (gameTime % 360) * (360 / 60.0f);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
            itemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
            pPoseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos) + 10;
        int sLight = level.getBrightness(LightLayer.SKY, pos) + 10;
        return LightTexture.pack(Math.min(bLight, 15), Math.min(sLight, 15));
    }
}
