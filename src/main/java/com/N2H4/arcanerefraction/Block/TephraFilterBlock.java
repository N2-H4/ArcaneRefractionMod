package com.N2H4.arcanerefraction.Block;

import com.N2H4.arcanerefraction.BlockEntity.TephraFilterEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TephraFilterBlock extends AmethystFilterBlock 
{
    public TephraFilterBlock()
    {
        super();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TephraFilterEntity(pos, state);
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
            if (blockentity instanceof TephraFilterEntity && pPlayer instanceof ServerPlayer) 
            {
                pPlayer.openMenu((TephraFilterEntity)blockentity,pPos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
