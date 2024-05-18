package com.N2H4.arcanerefraction.Block;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.N2H4.arcanerefraction.BlockEntity.RegolithFocusEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RegolithFocusBlock extends AmethystFocusBlock 
{
    public RegolithFocusBlock()
    {
        super();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RegolithFocusEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
                return (lvl, pos, st, blockEntity) -> {
                    if (blockEntity instanceof RegolithFocusEntity be) {
                        be.tickServer();
                    }
                };
        } else {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof RegolithFocusEntity be) {
                    be.tickServer();
                }
            };
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) 
    {
        if (pLevel.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        if (pHand == InteractionHand.MAIN_HAND && pPlayer.isHolding(Items.BRUSH))
        {
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            ((RegolithFocusEntity)tile).interact();
            return InteractionResult.SUCCESS;
        }
        else
        {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof RegolithFocusEntity && pPlayer instanceof ServerPlayer) 
            {
                pPlayer.openMenu((RegolithFocusEntity)blockentity,pPos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!level.isClientSide() && newState.getBlock()!=state.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RegolithFocusEntity blockEntity) {
                ItemStackHandler inventory = blockEntity.getInventory();
                for (int index = 0; index < inventory.getSlots(); index++) {
                    ItemStack stack = inventory.getStackInSlot(index);
                    var entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                    level.addFreshEntity(entity);
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
