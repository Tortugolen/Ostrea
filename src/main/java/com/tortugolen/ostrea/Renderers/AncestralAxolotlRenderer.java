package com.tortugolen.ostrea.Renderers;

import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class AncestralAxolotlRenderer extends AxolotlRenderer {
    public AncestralAxolotlRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(Axolotl pEntity) {
        return new ResourceLocation(Ostrea.MOD_ID, "textures/entity/ancestral_axolotl.png");
    }
}
