package com.N2H4.arcanerefraction.Block;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.N2H4.arcanerefraction.BlockEntity.AmethystFilterEntity;
import com.N2H4.arcanerefraction.BlockEntity.AmethystFocusEntity;
import com.N2H4.arcanerefraction.Utils.ILensPart;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;


public class AmethystFilterBlock extends HalfTransparentBlock implements EntityBlock, ILensPart 
{
    AmethystFocusEntity master;
    
    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    public AmethystFilterBlock() 
    {
        super(Blocks.AMETHYST_BLOCK.properties().noOcclusion().isViewBlocking(AmethystFilterBlock::never));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AmethystFilterEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) 
    {
        if (pLevel.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        else
        {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof AmethystFilterEntity && pPlayer instanceof ServerPlayer) 
            {
                pPlayer.openMenu((AmethystFilterEntity)blockentity,pPos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if(master!=null)
            master.getUpdated();
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    public void onRemove(@NonNls BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public void setMaster(AmethystFocusEntity m)
    {
        master=m;
    }

    @Override
    public float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
        return true;
    }
    
}
