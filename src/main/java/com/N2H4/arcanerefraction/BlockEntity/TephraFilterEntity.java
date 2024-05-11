package com.N2H4.arcanerefraction.BlockEntity;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.TEPHRA_FILTER_ENTITY;

import com.N2H4.arcanerefraction.Menu.TephraFilterMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class TephraFilterEntity extends AmethystFilterEntity 
{
    public TephraFilterEntity(BlockPos pos, BlockState state)
    {
        super(pos,state,TEPHRA_FILTER_ENTITY.get());
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TephraFilterMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.arcanerefraction.tephra_filter_menu");
    }
}
