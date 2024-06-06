package com.N2H4.arcanerefraction.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import java.util.List;

import com.N2H4.arcanerefraction.client.gui.ChapterButton;
import com.N2H4.arcanerefraction.client.gui.GuideContent;

import org.jetbrains.annotations.NotNull;

public class ARGuideScreen extends Screen 
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/ar_guide_bg4.png");
    private PageButton forwardButton;
    private PageButton backButton;
    private PageButton startButton;
    private ChapterButton basicsButton;
    private ChapterButton lensSizesButton;
    private ChapterButton effectsButton;
    private ChapterButton transformationButton;
    private GuideContent content;
    private String currenChapter;
    private int currentPage;
    private boolean firstTimeOpened;

    public ARGuideScreen()
    {
        super(CommonComponents.EMPTY);
        currenChapter="intro";
        currentPage=0;
        content=new GuideContent();
        content.loadGuide();
        firstTimeOpened=true;
    }


    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.blit(TEXTURE, (this.width-256)/2, 50, 0, 0, 256, 256);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        drawPage(pGuiGraphics);
    }

    private void setStartPage()
    {
        currenChapter="intro";
        currentPage=0;
        this.forwardButton.visible=false;
        this.backButton.visible=false;
        this.startButton.visible=false;
        this.basicsButton.visible=true;
        this.lensSizesButton.visible=true;
        this.effectsButton.visible=true;
        this.transformationButton.visible=true;
    }

    private void setChapterPage(String chapter)
    {
        currenChapter=chapter;
        if(getMaxPages()>1)
        {
            this.forwardButton.visible=true;
        }
        this.startButton.visible=true;
        this.basicsButton.visible=false;
        this.lensSizesButton.visible=false;
        this.effectsButton.visible=false;
        this.transformationButton.visible=false;
    }


    private int getMaxPages()
    {
        return content.getChapterPages(currenChapter+"Left");
    }

    private void drawPage(GuiGraphics pGuiGraphics)
    {  
        //draw left
        int lineOffset=105;
        List<String> lines=content.getChapterPage(currenChapter+"Left", currentPage);
        for (String line : lines)
        {
            drawSmallString(pGuiGraphics, line,198, lineOffset);
            lineOffset+=10;
        }
        //draw right
        lineOffset=105;
        lines=content.getChapterPage(currenChapter+"Right", currentPage);
        for (String line : lines)
        {
            drawSmallString(pGuiGraphics, line,-20, lineOffset);
            lineOffset+=10;
        }
    }

    private void drawSmallString(GuiGraphics pGuiGraphics, String line,int position, int lineOffset)
    {
        var pose = pGuiGraphics.pose();
        pose.pushPose();
        {
            pose.scale(0.6f, 0.6f, 0.6f);
            pGuiGraphics.drawString(minecraft.font, line, ((this.width*0.5f)/0.6f)-position, lineOffset, 1184274 ,false);
        }
        pose.popPose();
    }

    protected void createPageControlButtons() {
        int i = (this.width - 256) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 225, 182, true, p_98297_ -> this.pageForward(), false));
        this.backButton = this.addRenderableWidget(new PageButton(i+1, 182, false, p_98287_ -> this.pageBack(), false));
        this.startButton = this.addRenderableWidget(new PageButton(i+1, 50, false, p_98287_ -> this.pageStart(), false));
    }

    protected void createStartControls() {
        this.basicsButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 65, true, p_98297_ -> this.setChapterPage("basics"),"Basics"));
        this.lensSizesButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 82, true, p_98297_ -> this.setChapterPage("sizes"),"Lens Sizes"));
        this.effectsButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 99, true, p_98297_ -> this.setChapterPage("effects"),"Effects"));
        this.transformationButton=this.addRenderableWidget(new ChapterButton((this.width/2)+17, 116, true, p_98297_ -> this.setChapterPage("transformations"),"Transformations"));
    }

    @Override
    protected void init() {
        this.createPageControlButtons();
        this.createStartControls();
        if(firstTimeOpened)
        {
            firstTimeOpened=false;
            this.setStartPage();
        }
    }

    @Override
    protected void rebuildWidgets() {
        super.rebuildWidgets();
        if(currenChapter!="intro")
        {
            if(getMaxPages()>1 && currentPage!=getMaxPages()-1)
            {
                this.forwardButton.visible=true;
            }
            else
            {
                this.forwardButton.visible=false;
            }
            this.startButton.visible=true;
            this.basicsButton.visible=false;
            this.lensSizesButton.visible=false;
            this.effectsButton.visible=false;
            this.transformationButton.visible=false;
        }
        else
        {
            this.forwardButton.visible=false;
            this.startButton.visible=false;
            this.backButton.visible=false;
            this.basicsButton.visible=true;
            this.lensSizesButton.visible=true;
            this.effectsButton.visible=true;
            this.transformationButton.visible=true;
        }
    }

    private void pageBack() {
        this.forwardButton.visible=true;
        if(currentPage>0)
        {
            currentPage--;
        }
        if(currentPage==0)
        {
            this.backButton.visible=false;
        }
    }

    private void pageStart() 
    {
        currenChapter="intro";
        currentPage=0;
        setStartPage();
    }


    private void pageForward() 
    {
        this.backButton.visible=true;
        if(currentPage<getMaxPages()-1)
        {
            currentPage++;
        }
        if(currentPage==getMaxPages()-1)
        {
            this.forwardButton.visible=false;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
}
