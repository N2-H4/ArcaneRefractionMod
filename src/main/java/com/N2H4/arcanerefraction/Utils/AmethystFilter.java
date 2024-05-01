package com.N2H4.arcanerefraction.Utils;

import java.util.Dictionary;
import java.util.Enumeration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Hashtable;

public class AmethystFilter 
{
    private Dictionary<BlockPos,List<Item>> dict;

    public AmethystFilter()
    {
        dict = new Hashtable<>();
    }

    public void insertItem(BlockPos pos, Item item)
    {
        if(dict.get(pos)==null)
        {
            dict.put(pos, new ArrayList<Item>());
            dict.get(pos).add(item);
        }
        else
        {
            if(!dict.get(pos).contains(item))
            {
                dict.get(pos).add(item);
            }
        }
    }

    public boolean contains(Item item)
    {
        Enumeration<BlockPos> k = dict.keys();
        while (k.hasMoreElements()) 
        {
            BlockPos key = k.nextElement();
            if(dict.get(key).contains(item))
                return true;
        }
        return false;
    }

    public BlockPos getPos(Item item)
    {
        Enumeration<BlockPos> k = dict.keys();
        while (k.hasMoreElements()) 
        {
            BlockPos key = k.nextElement();
            if(dict.get(key).contains(item))
                return key;
        }
        return null;
    }
    
}
