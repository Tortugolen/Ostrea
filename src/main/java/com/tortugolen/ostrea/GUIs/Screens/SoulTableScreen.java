package com.tortugolen.ostrea.GUIs.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tortugolen.ostrea.GUIs.Menus.SoulTableMenu;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SoulTableScreen extends AbstractContainerScreen<SoulTableMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/soul_table.png");

    public SoulTableScreen(SoulTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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
        renderSoulsBar(guiGraphics, x, y);
        renderSoulsProgress(guiGraphics, x, y);
        renderEffectSoulsBar(guiGraphics, x, y);
        renderEffectSoulsProgress(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 137, y + 30, 176, 8, 8, menu.getScaledProgress());
        }
    }

    public void renderSoulsBar(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 70, y + 11 + 64 - menu.getScaledSouls(), 0, 166 + 64 - menu.getScaledSouls(), 4, menu.getScaledSouls());
    }

    public void renderSoulsProgress(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 49, y + 35, 184, 8, 8, menu.getScaledSoulsProgress());
    }

    public void renderEffectSoulsBar(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 102, y + 11 + 64 - menu.getScaledEffectSouls(), menu.getEffectSoulsBarEffect(), 166 + 64 - menu.getScaledEffectSouls(), 4, menu.getScaledEffectSouls());
    }

    public void renderEffectSoulsProgress(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 77, y + 16, 176, 0, menu.getScaledEffectSoulsProgress(), 8);
    }
}
