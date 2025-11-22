package com.tortugolen.ostrea.GUIs.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tortugolen.ostrea.GUIs.Menus.MechanicalOysterMenu;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MechanicalOysterScreen extends AbstractContainerScreen<MechanicalOysterMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Ostrea.MOD_ID, "textures/gui/mechanical_oyster.png");

    public MechanicalOysterScreen(MechanicalOysterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    private final int imageHeight = 166 + 17;


    @Override
    protected void init() {
        super.init();
        titleLabelY = -3;
        inventoryLabelY = 80;
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
        renderCalciumCarbonateBar(guiGraphics, x, y);
        renderCalciumCarbonateCycle(guiGraphics, x, y);
        renderModifier1(guiGraphics, x, y);
        renderModifier2(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 61, y + 47, 176, 0, menu.getScaledProgress(), 15);
        }
    }

    public void renderCalciumCarbonateBar(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 62, y + 84, 176, 16, menu.getScaledAmountCalciumCarbonate(), 4);
    }

    public void renderCalciumCarbonateCycle(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 17, y + 38, 0, 183, 34, menu.getScaledCyclesCalciumCarbonate());
    }

    public void renderModifier1(GuiGraphics guiGraphics, int x, int y) {
        if (menu.blockEntity.getActiveModifier1()) {
            guiGraphics.blit(TEXTURE, x + 71, y + 36, 176, 20, 16, 16);
        }
    }

    public void renderModifier2(GuiGraphics guiGraphics, int x, int y) {
        if (menu.blockEntity.getActiveModifier2()) {
            guiGraphics.blit(TEXTURE, x + 89, y + 36, 176, 20, 16, 16);
        }
    }
}
