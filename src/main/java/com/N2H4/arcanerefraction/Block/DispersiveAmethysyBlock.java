package com.N2H4.arcanerefraction.Block;

import com.N2H4.arcanerefraction.BlockEntity.AmethystFocusEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;


public class DispersiveAmethysyBlock extends HalfTransparentBlock /*implements EntityBlock*/
{
    AmethystFocusEntity master;
    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
    public DispersiveAmethysyBlock()
    {
        super(Blocks.AMETHYST_BLOCK.properties().noOcclusion().isViewBlocking(DispersiveAmethysyBlock::never));
    }

    /*@Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TestBlockEntity(pos, state);
    }*/

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if(master!=null)
            master.getUpdated();
        super.destroy(pLevel, pPos, pState);
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
