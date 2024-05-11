package com.N2H4.arcanerefraction.Block;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.N2H4.arcanerefraction.BlockEntity.AmethystFocusEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class AmethystFocusBlock extends HalfTransparentBlock implements EntityBlock
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    public AmethystFocusBlock() 
    {
        super(Blocks.AMETHYST_BLOCK.properties().noOcclusion().isViewBlocking(AmethystFocusBlock::never));
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED);
		super.createBlockStateDefinition(builder);
	}

    @Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(POWERED,
				context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AmethystFocusEntity(pos, state);
    }
    

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
                return (lvl, pos, st, blockEntity) -> {
                    if (blockEntity instanceof AmethystFocusEntity be) {
                        be.tickServer();
                    }
                };
        } else {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof AmethystFocusEntity be) {
                    be.tickServer();
                }
            };
        }
    }

    @Override
    public float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
        return true;
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
            ((AmethystFocusEntity)tile).interact();
            return InteractionResult.SUCCESS;
        }
        else
        {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof AmethystFocusEntity && pPlayer instanceof ServerPlayer) 
            {
                pPlayer.openMenu((AmethystFocusEntity)blockentity,pPos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(@Nonnull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AmethystFocusEntity blockEntity) {
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

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) 
        {
            boolean previouslyPowered = pState.getValue(BlockStateProperties.POWERED);
            //BlockEntity tile = pLevel.getBlockEntity(pPos);
            if(previouslyPowered != pLevel.hasNeighborSignal(pPos))
            {
                pLevel.setBlock(pPos, pState.cycle(BlockStateProperties.POWERED), 2 | 16);
            }
        }
    }

    

}
