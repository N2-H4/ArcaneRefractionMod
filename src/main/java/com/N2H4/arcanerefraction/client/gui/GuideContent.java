package com.N2H4.arcanerefraction.client.gui;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class GuideContent 
{
    private ResourceManager manager=Minecraft.getInstance().getResourceManager();
    private static final ResourceLocation location = new ResourceLocation(MODID, "ar_guide_content.xml");

    private Dictionary<String, List<List<String>>> content;

    public GuideContent()
    {
        content = new Hashtable<>();
        content.put("introLeft", new ArrayList<>());
        content.put("introRight", new ArrayList<>());
        content.put("basicsLeft", new ArrayList<>());
        content.put("basicsRight", new ArrayList<>());
        content.put("sizesLeft", new ArrayList<>());
        content.put("sizesRight", new ArrayList<>());
        content.put("effectsLeft", new ArrayList<>());
        content.put("effectsRight", new ArrayList<>());
        content.put("transformationsLeft", new ArrayList<>());
        content.put("transformationsRight", new ArrayList<>());
    }

    public List<String> getChapterPage(String chapter, int page)
    {
        return content.get(chapter).get(page);
    }

    public int getChapterPages(String chapter)
    {
        return content.get(chapter).size();
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

        //parse intro
        List<String> linesList;
        NodeList lines;
        NodeList pages = doc.getElementsByTagName("intro").item(0).getChildNodes();
        for(int i=1;i<pages.getLength()-1;i+=2)
        {
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(1).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("introLeft").add(linesList);
            this.content.get("introRight").add(new ArrayList<>());
        }

        //parse basics
        pages = doc.getElementsByTagName("basics").item(0).getChildNodes();
        for(int i=1;i<pages.getLength()-1;i+=2)
        {
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(1).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("basicsLeft").add(linesList);
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(3).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("basicsRight").add(linesList);
        }

        //parse sizes
        pages = doc.getElementsByTagName("sizes").item(0).getChildNodes();
        for(int i=1;i<pages.getLength()-1;i+=2)
        {
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(1).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("sizesLeft").add(linesList);
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(3).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("sizesRight").add(linesList);
        }

        //parse effects
        pages = doc.getElementsByTagName("effects").item(0).getChildNodes();
        for(int i=1;i<pages.getLength()-1;i+=2)
        {
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(1).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("effectsLeft").add(linesList);
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(3).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("effectsRight").add(linesList);
        }

        //parse transformations
        pages = doc.getElementsByTagName("transformations").item(0).getChildNodes();
        for(int i=1;i<pages.getLength()-1;i+=2)
        {
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(1).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("transformationsLeft").add(linesList);
            linesList=new ArrayList<>();
            lines=pages.item(i).getChildNodes().item(3).getChildNodes();
            for(int j=1;j<lines.getLength()-1;j+=2)
            {
                linesList.add(lines.item(j).getTextContent());
            }
            this.content.get("transformationsRight").add(linesList);
        }

    }
    
}
