package com.tortugolen.ostrea.Base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tortugolen.ostrea.Items.AxolotlHelmetItem;
import com.tortugolen.ostrea.Items.ImprovedAxolotlHelmetItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AxolotlHelmetVanillaHide<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public AxolotlHelmetVanillaHide(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack helmet = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);

        if (helmet.getItem() instanceof AxolotlHelmetItem || helmet.getItem() instanceof ImprovedAxolotlHelmetItem) {
            return;
        }
    }
}
