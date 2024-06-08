package com.N2H4.arcanerefraction.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.N2H4.arcanerefraction.client.gui.ChapterButton;
import com.N2H4.arcanerefraction.client.gui.GuideContent;

import org.jetbrains.annotations.NotNull;

public class ARGuideScreen extends Screen 
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/ar_guide_bg4.png");
    private static final ResourceLocation ASSETS = new ResourceLocation(MODID, "textures/gui/ar_guide_assets.png");
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
    private Dictionary<String,ItemStack> chachedItemStacks;

    public ARGuideScreen()
    {
        super(CommonComponents.EMPTY);
        currenChapter="intro";
        currentPage=0;
        content=new GuideContent();
        content.loadGuide();
        firstTimeOpened=true;
        chachedItemStacks=new Hashtable<>();
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
            if(line.length()>=1 && line.charAt(0)=='@')
            {
                String[] args=line.split("@",7);
                drawImage(pGuiGraphics, ASSETS, Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));
            }
            else if(line.length()>=1 && line.charAt(0)=='#')
            {
                drawRecipe(pGuiGraphics, line, ASSETS);
            }
            else
                drawSmallString(pGuiGraphics, line,198, lineOffset);
            lineOffset+=10;
        }
        //draw right
        lineOffset=105;
        lines=content.getChapterPage(currenChapter+"Right", currentPage);
        for (String line : lines)
        {
            if(line.length()>=1 && line.charAt(0)=='@')
            {
                String[] args=line.split("@",7);
                drawImage(pGuiGraphics, ASSETS, Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));
            }
            else if(line.length()>=1 && line.charAt(0)=='#')
            {
                drawRecipe(pGuiGraphics, line, ASSETS);
            }
            else
                drawSmallString(pGuiGraphics, line,-20, lineOffset);
            lineOffset+=10;
        }
    }

    private void drawImage(GuiGraphics pGuiGraphics, ResourceLocation resource,int x, int y, int xOffset, int yOffset, int width, int height)
    {
        var pose = pGuiGraphics.pose();
        pose.pushPose();
        {
            pose.scale(2, 2, 2);
            pGuiGraphics.blit(resource, (int)((this.width*0.5f)/2)-x, y, xOffset, yOffset, width, height);
        }
        pose.popPose();
    }

    private void drawRecipe(GuiGraphics pGuiGraphics, String line, ResourceLocation resource)
    {
        String[] args=line.split("#",12);
        int x = Integer.valueOf(args[1]);
        int y = Integer.valueOf(args[2]);
        var pose = pGuiGraphics.pose();
        pose.pushPose();
        {
            pose.scale(0.36f, 0.36f, 0.36f);
            pose.translate(x/0.36f, y/0.36, 0);
           // pGuiGraphics.blit(resource, (int)(((this.width*0.5f)-x)/0.36f), y, 49, 3, 111, 109);
           pGuiGraphics.blit(resource, (int)((this.width*0.5f)/0.36f), 0, 49, 3, 111, 109);
        }
        pose.popPose();
        pose.pushPose();
        {
            //int bgOffset=(int)((this.width*0.5f)/0.36f)-x;
            pose.scale(0.68f, 0.68f, 0.68f);
            pose.translate(x/0.68, y/0.68, 0);
            //pGuiGraphics.renderItem(new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft:stone"))), (int)((this.width*0.5f)/0.68f), 0);
            //top row
            if(args[3].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[3])==null)
                    chachedItemStacks.put(args[3], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[3]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[3]), (int)((this.width*0.5f)/0.68f)+3, 2);
            }
            if(args[4].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[4])==null)
                    chachedItemStacks.put(args[4], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[4]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[4]), (int)((this.width*0.5f)/0.68f)+22, 2);
            }
            if(args[5].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[5])==null)
                    chachedItemStacks.put(args[5], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[5]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[5]), (int)((this.width*0.5f)/0.68f)+41, 2);
            }
            //mid row
            if(args[6].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[6])==null)
                    chachedItemStacks.put(args[6], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[6]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[6]), (int)((this.width*0.5f)/0.68f)+3, 21);
            }
            if(args[7].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[7])==null)
                    chachedItemStacks.put(args[7], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[7]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[7]), (int)((this.width*0.5f)/0.68f)+22, 21);
            }
            if(args[8].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[8])==null)
                    chachedItemStacks.put(args[8], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[8]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[8]), (int)((this.width*0.5f)/0.68f)+41, 21);
            }
            //bot row
            if(args[9].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[9])==null)
                    chachedItemStacks.put(args[9], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[9]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[9]), (int)((this.width*0.5f)/0.68f)+3, 40);
            }
            if(args[10].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[10])==null)
                    chachedItemStacks.put(args[10], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[10]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[10]), (int)((this.width*0.5f)/0.68f)+22, 40);
            }
            if(args[11].charAt(0)!='!')
            {
                if(chachedItemStacks.get(args[11])==null)
                    chachedItemStacks.put(args[11], new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(args[11]))));
                pGuiGraphics.renderItem(chachedItemStacks.get(args[11]), (int)((this.width*0.5f)/0.68f)+41, 40);
            }
        }
        pose.popPose();
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
            if(currentPage!=0)
            {
                this.backButton.visible=true;
            }
            else
            {
                this.backButton.visible=false;
            }
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
