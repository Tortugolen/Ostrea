package com.tortugolen.ostrea.GUIs.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tortugolen.ostrea.GUIs.Menus.CrusherMenu;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrusherScreen extends AbstractContainerScreen<CrusherMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/crusher.png");

    public CrusherScreen(CrusherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressBar(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 74, y + 35, 176, 0, menu.getScaledProgress(), 16);
        }
    }
}
