package com.N2H4.arcanerefraction.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import org.jetbrains.annotations.NotNull;

import com.N2H4.arcanerefraction.Menu.TephraFilterMenu;

public class TephraFilterScreen extends AbstractContainerScreen<TephraFilterMenu>
{
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MODID, "textures/gui/amethyst_filter_gui.png");

    public TephraFilterScreen(TephraFilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelX=8;
        this.inventoryLabelY=128;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderTransparentBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
