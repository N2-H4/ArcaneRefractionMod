package com.N2H4.arcanerefraction.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import org.jetbrains.annotations.NotNull;

public class ARGuideScreen extends Screen 
{
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MODID, "textures/gui/amethyst_focus_gui.png");

    public ARGuideScreen()
    {
        super(CommonComponents.EMPTY);

    }


    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.blit(TEXTURE, (this.width - 192) / 2, 2, 0, 0, 192, 192);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
    
}
