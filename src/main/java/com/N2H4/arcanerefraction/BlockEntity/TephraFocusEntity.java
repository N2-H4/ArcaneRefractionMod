package com.N2H4.arcanerefraction.BlockEntity;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.LENS_SOUND;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.TEPHRA_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.TEPHRA_FOCUS_ENTITY;

import com.N2H4.arcanerefraction.Block.TephraFilterBlock;
import com.N2H4.arcanerefraction.Menu.TephraFocusMenu;
import com.N2H4.arcanerefraction.Utils.ILensPart;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TephraFocusEntity extends AmethystFocusEntity
{
    public TephraFocusEntity(BlockPos pos, BlockState state) 
    {
        super(pos, state, TEPHRA_FOCUS_ENTITY.get());
    }

    @Override
    public void tickServer() 
    {
        if(level.isClientSide())
        {
            ray_cooldown++;
            if(is_formed && sky_access && ray_cooldown>120)
            {
                ray_cooldown=0;
                spawnParticles();
            }
            return;
        }
        if (!level.isClientSide() && is_formed)
        {
            timer++;
            timer2++;
            sound_timer+=level.getRandom().nextInt(5);
            if (timer > 20)
            {
                timer = 0;
                int coated_size=selectMode();
                if(lens_size>coated_size)
                    lens_size=coated_size;
                checkSkyAccess();
                scanUnder();
                this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
            if(sound_timer>sound_cooldown)
            {
                sound_timer=0;
                level.playSound(null, worldPosition, LENS_SOUND.get(), SoundSource.BLOCKS,0.4f,1.0f);
            }
            if(timer2>40 && sky_access)
            {
                timer2=0;
                switch(mode)
                {
                    case OFF:
                    {
                        break;
                    }
                    case TRANSFORM:
                    {
                        break;
                    }
                    case GROW:
                    {
                        grow();
                        break;
                    }
                    case SPAWN:
                    {
                        spawnHostile();
                        break;
                    }
                    case HEALDMG:
                    {
                        heal();
                        break;
                    }
                    case MELTFREEZE:
                    {
                        melt();
                        break;
                    }
                    case GATHER:
                    {
                        mineBlocks(false);
                        break;
                    }
                    case GENERATE:
                    {
                        shedItems();
                        break;
                    }
                    case COLLECT:
                    {
                        collectItems();
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void scanUnder()
    {
        processed_positions.clear();
        int size = lens_size != 5 ? lens_size : 6;
        for(int i=-size;i<=size;i++)
        {
            for(int j=-size;j<=size;j++)
            {
                BlockPos pos=worldPosition.offset(i,0,j);
                Block b=level.getBlockState(pos).getBlock();
                if(b instanceof ILensPart || b == TEPHRA_FOCUS_BLOCK.get())
                {
                    if(b instanceof TephraFilterBlock)
                    {
                        TephraFilterEntity be=(TephraFilterEntity)level.getBlockEntity(pos);
                        ItemStackHandler inv=be.getInventory();
                        int slot_count=inv.getSlots();
                        for(int s=0;s<slot_count;s++)
                        {
                            Item item=inv.getStackInSlot(s).getItem();
                            if(item!=Items.AIR && !filtered_items.contains(item))
                                filtered_items.insertItem(pos, item);
                        }
                    }

                    for(int depth=1;depth<=depth_range;depth++)
                    {
                        pos=worldPosition.offset(i,-depth,j);
                        b=level.getBlockState(pos).getBlock();
                        if(b!=Blocks.AIR && !(b instanceof ILensPart) && b!=AMETHYST_FOCUS_BLOCK.get() && b!=REGOLITH_FOCUS_BLOCK.get() && b!=TEPHRA_FOCUS_BLOCK.get())
                        {
                            processed_positions.add(pos);
                            break;
                        }
                    }
                } 
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) 
    {
        return new TephraFocusMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    public Component getDisplayName() 
    {
        return Component.translatable("container.arcanerefraction.tephra_focus_menu");
    }
}
