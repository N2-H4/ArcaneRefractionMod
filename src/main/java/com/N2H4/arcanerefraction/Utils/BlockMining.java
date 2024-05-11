package com.N2H4.arcanerefraction.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class BlockMining 
{
    public static boolean canMine(Level level, BlockState state, BlockPos pos, boolean silk_touch)
    {
        FakePlayerMiner player = FakePlayerMiner.setupFakePlayer((ServerLevel) level);
        ItemStack stack = Items.DIAMOND_PICKAXE.getDefaultInstance();
        if(silk_touch)
            stack.enchant(Enchantments.SILK_TOUCH, 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        boolean canMine = !NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player)).isCanceled();
        player.cleanupFakePlayer((ServerLevel) level);
        return canMine;
    }

    public static void dropResources(Level level, BlockState state, BlockPos pos, boolean silk_touch) {
        if (state.isAir()) 
        {
            return;
        }
        ItemStack stack = Items.DIAMOND_PICKAXE.getDefaultInstance();
        if(silk_touch)
            stack.enchant(Enchantments.SILK_TOUCH, 1);
        FakePlayerMiner player = FakePlayerMiner.setupFakePlayer((ServerLevel)level);
        Block.dropResources(state, level, pos, level.getBlockEntity(pos), player, stack);
        level.levelEvent(player, 2001, pos, Block.getId(state));
        level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, state));
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, Blocks.AIR.defaultBlockState()));
        player.cleanupFakePlayer((ServerLevel)level);
    }
}
