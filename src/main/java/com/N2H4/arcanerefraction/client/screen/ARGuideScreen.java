package com.N2H4.arcanerefraction.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;
import com.N2H4.arcanerefraction.client.gui.ChapterButton;
import com.N2H4.arcanerefraction.client.gui.GuideContent;

import org.jetbrains.annotations.NotNull;

public class ARGuideScreen extends Screen 
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/ar_guide_bg4.png");
    private PageButton forwardButton;
    private PageButton backButton;
    private ChapterButton basicsButton;
    private ChapterButton lensSizesButton;
    private ChapterButton effectsButton;
    private ChapterButton transformationButton;
    private GuideContent content;

    public ARGuideScreen()
    {
        super(CommonComponents.EMPTY);
        content=new GuideContent();
        content.loadGuide();
    }


    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.blit(TEXTURE, (this.width-256)/2, 50, 0, 0, 256, 256);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        drawStartPage(pGuiGraphics);
    }

    private void setStartPage()
    {
        this.forwardButton.visible=false;
        this.backButton.visible=false;
        this.basicsButton.visible=true;
        this.lensSizesButton.visible=true;
        this.effectsButton.visible=true;
        this.transformationButton.visible=true;
    }

    private void drawStartPage(GuiGraphics pGuiGraphics)
    {  
        int lineOffset=57;
        for (String line : content.getStartChapter().get(0)) 
        {
            drawSmallString(pGuiGraphics, line,lineOffset);
            lineOffset+=10;
        }
    }

    private void drawSmallString(GuiGraphics pGuiGraphics, String line,int lineOffset)
    {
        var pose = pGuiGraphics.pose();
        pose.pushPose();
        {
            pose.translate(40, 15, 0);
            pose.scale(0.75f, 0.75f, 1f);
            pGuiGraphics.drawString(minecraft.font, line, ((this.width-256)/2)+10, lineOffset, 1184274 ,false);
        }
        pose.popPose();
    }

    protected void createPageControlButtons() {
        int i = (this.width - 256) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 225, 182, true, p_98297_ -> this.pageForward(), false));
        this.backButton = this.addRenderableWidget(new PageButton(i+1, 182, false, p_98287_ -> this.pageBack(), false));
    }

    protected void createStartControls() {
        this.basicsButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 65, true, p_98297_ -> this.pageForward(),"Basics"));
        this.lensSizesButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 82, true, p_98297_ -> this.pageForward(),"Lens Sizes"));
        this.effectsButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 99, true, p_98297_ -> this.pageForward(),"Effects"));
        this.transformationButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 116, true, p_98297_ -> this.pageForward(),"Transformations"));
    }

    @Override
    protected void init() {
        this.createPageControlButtons();
        this.createStartControls();
        this.setStartPage();
    }

    private void pageBack() {
        System.out.println("left");
    }


    private void pageForward() {
        System.out.println("right");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
}
