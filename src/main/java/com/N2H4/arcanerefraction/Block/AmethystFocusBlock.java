package com.N2H4.arcanerefraction.Block;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import com.N2H4.arcanerefraction.BlockEntity.AmethystFocusEntity;

import net.minecraft.core.BlockPos;
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
            return null;
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
			return InteractionResult.SUCCESS;
        if (pHand == InteractionHand.MAIN_HAND)
        {
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            ((AmethystFocusEntity)tile).interact();
            return InteractionResult.SUCCESS;
        }
        //return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        else
        {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) 
        {
            boolean previouslyPowered = pState.getValue(BlockStateProperties.POWERED);
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            if(previouslyPowered != pLevel.hasNeighborSignal(pPos))
            {
                pLevel.setBlock(pPos, pState.cycle(BlockStateProperties.POWERED), 2 | 16);
            }
        }
    }

    

}
