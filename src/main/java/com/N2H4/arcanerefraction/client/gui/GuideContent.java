package com.N2H4.arcanerefraction.client.gui;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class GuideContent 
{
    private ResourceManager manager=Minecraft.getInstance().getResourceManager();
    private static final ResourceLocation location = new ResourceLocation(MODID, "ar_guide_content.xml");

    private String tmp="";

    private List<List<String>> intro;

    public GuideContent()
    {
        intro=new ArrayList<>();
    }

    public List<List<String>> getStartChapter()
    {
        return intro;
    }

    public void loadGuide()
    {
        Resource bookResource;
        try
        {
            bookResource = manager.getResourceOrThrow(location);
        }
        catch (IOException e)
        {
            bookResource = null;
        }
        
        if(bookResource!=null)
        {
            try (InputStream stream = bookResource.open())
            {
                parseFile(stream);
            }
            catch (IOException e)
            {
                return;
            }
        }
    }

    public void parseFile(InputStream stream)
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc;
        try 
        {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(stream);
        } 
        catch (Exception e) 
        {
            return;
        }

        doc.getDocumentElement().normalize();

        tmp=doc.getElementsByTagName("line").item(0).getTextContent();

        //parse intro
        NodeList pages = doc.getElementsByTagName("intro").item(0).getChildNodes();
        for(int i=1;i<pages.getLength()-1;i++)
        {
            List<String> linesList=new ArrayList<>();
            NodeList lines=pages.item(i).getChildNodes().item(1).getChildNodes();
            System.out.println(lines.getLength());
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.intro.add(linesList);
        }
    }

    public String getTmp()
    {
        return tmp;
    }
    
}
