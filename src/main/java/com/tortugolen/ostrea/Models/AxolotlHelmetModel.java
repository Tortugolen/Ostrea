package com.tortugolen.ostrea.Models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class AxolotlHelmetModel extends EntityModel<Player> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Ostrea.MOD_ID, "axolotl_helmet"), "main");
	public final ModelPart head;

	public AxolotlHelmetModel(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		CubeDeformation deformation = new CubeDeformation(0.0F);
		MeshDefinition mesh = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition root = mesh.getRoot();
		PartDefinition head = root.getChild("head");
		PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F)), PartPose.ZERO);
		PartDefinition antenna = helmet.addOrReplaceChild("antenna", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));
		antenna.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -4.0F, 0.0F, 3, 4, 0).addBox(-1.5F, 0.0F, 0.0F, 3, 4, 0).addBox(1.0F, -4.625F, 0.0F, 3, 3, 0), PartPose.offset(-6.25F, -6.0F, 0.0F));
		antenna.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.5F, -4.0F, 0.0F, 3, 4, 0).addBox(-1.5F, 0.0F, 0.0F, 3, 4, 0).addBox(-4F, -4.625F, 0.0F, 3, 3, 0), PartPose.offset(6.25F, -6.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(Player pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		head.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}
}