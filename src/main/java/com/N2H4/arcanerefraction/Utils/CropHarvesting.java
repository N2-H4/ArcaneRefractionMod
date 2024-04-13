package com.N2H4.arcanerefraction.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;

public class CropHarvesting 
{

    public static boolean isAllowedCrop(Block b)
    {
        return b!=Blocks.GRASS_BLOCK && !(b instanceof DoublePlantBlock);
    }

    public static boolean canHarvest(Level world, BlockPos pos) {
        BlockState at = world.getBlockState(pos);
        if (!(at.getBlock() instanceof IPlantable)) return false;
        if (!isAllowedCrop(at.getBlock())) return false;
        if (at.getBlock() instanceof StemBlock) return false;
        return true;
    }

    public static void harvestAndReplant(BlockState bs, Level level, BlockPos pos)
    {
        if(!canHarvest(level, pos)) return;

        //Crop
        if(bs.getBlock() instanceof CropBlock)
        {
            if(((CropBlock)bs.getBlock()).isMaxAge(bs))
            {
                Block.dropResources(bs, level, pos);
                level.setBlock(pos, bs.getBlock().defaultBlockState(), 2);
            }
            return;
        }

        //SugarCane
        if(bs.getBlock() instanceof SugarCaneBlock)
        {
            if(level.getBlockState(pos.below()).getBlock().equals(Blocks.SUGAR_CANE) && !level.getBlockState(pos.above()).getBlock().equals(Blocks.SUGAR_CANE))
            {
                Block.dropResources(bs, level, pos);
                level.removeBlock(pos, false);
            }
            return;
        }

        //Cactus
        if(bs.getBlock() instanceof CactusBlock)
        {
            if(level.getBlockState(pos.below()).getBlock().equals(Blocks.CACTUS) && !level.getBlockState(pos.above()).getBlock().equals(Blocks.CACTUS))
            {
                Block.dropResources(bs, level, pos);
                level.removeBlock(pos, false);
            }
            return;
        }

        //NetherWart
        if(bs.getBlock() instanceof NetherWartBlock)
        {
            if(!((NetherWartBlock)bs.getBlock()).isRandomlyTicking(bs))
            {
                Block.dropResources(bs, level, pos);
                level.setBlock(pos, bs.getBlock().defaultBlockState(), 2);
            }
            return;
        }
    }
}
