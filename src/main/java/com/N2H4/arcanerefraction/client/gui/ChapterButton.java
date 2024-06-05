package com.N2H4.arcanerefraction.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

@OnlyIn(Dist.CLIENT)
public class ChapterButton extends Button 
{
    private static final ResourceLocation CHAPTER_SPRITE = new ResourceLocation(MODID, "textures/gui/ar_guide_sprites.png");
    int textColor = 1184274;
    Minecraft minecraft;
    FormattedCharSequence formattedcharsequence;

    public ChapterButton(int pX, int pY, boolean pIsForward, Button.OnPress pOnPress, String text) {
        super(pX, pY, 100, 14, Component.literal(text), pOnPress, DEFAULT_NARRATION);
        minecraft = Minecraft.getInstance();
        formattedcharsequence = this.getMessage().getVisualOrderText();
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int offset = this.isHovered() ? 15 : 0;
        pGuiGraphics.blit(CHAPTER_SPRITE, this.getX(), this.getY(), 0, offset, 100, 14);
        pGuiGraphics.drawString(minecraft.font, this.getMessage(), this.getX()+42-minecraft.font.width(formattedcharsequence) / 2, this.getY()+3, textColor | Mth.ceil(this.alpha * 255.0F) << 24,false);
    }
}
