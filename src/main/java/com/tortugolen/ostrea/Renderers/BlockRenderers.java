package com.tortugolen.ostrea.Renderers;

import com.tortugolen.ostrea.Init.InitBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class BlockRenderers {
    public static void registerBlockRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.ARAGONITE_SHARD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.ARAGONITE_CLUSTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.CRUSHER.get(), RenderType.cutout());
    }
}
