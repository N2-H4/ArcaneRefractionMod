package com.N2H4.arcanerefraction.BlockEntity;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.LENS_SOUND;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_FOCUS_ENTITY;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.TEPHRA_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_PARTICLE;

import com.N2H4.arcanerefraction.Block.RegolithFilterBlock;
import com.N2H4.arcanerefraction.Menu.RegolithFocusMenu;
import com.N2H4.arcanerefraction.Utils.ILensPart;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RegolithFocusEntity extends AmethystFocusEntity
{
    public RegolithFocusEntity(BlockPos pos, BlockState state) 
    {
        super(pos, state, REGOLITH_FOCUS_ENTITY.get());
    }

    @Override
    public void tickServer() 
    {
        if(level.isClientSide())
        {
            ray_timer++;
            if(is_formed && !this.getBlockState().getValue(BlockStateProperties.POWERED) && sky_access && ray_timer>ray_cooldown)
            {
                ray_timer=0;
                spawnParticles((SimpleParticleType)REGOLITH_PARTICLE.get());
            }
            return;
        }
        if (!level.isClientSide() && is_formed && !this.getBlockState().getValue(BlockStateProperties.POWERED))
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
            if(sound_timer>sound_cooldown && sky_access)
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
                        harvestAndReplant();
                        break;
                    }
                    case SPAWN:
                    {
                        spawnPassive();
                        break;
                    }
                    case HEALDMG:
                    {
                        hurt();
                        break;
                    }
                    case MELTFREEZE:
                    {
                        freeze();
                        break;
                    }
                    case GATHER:
                    {
                        mineBlocks(true);
                        break;
                    }
                    case GENERATE:
                    {
                        spawnOres();
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
                if(b instanceof ILensPart || b == REGOLITH_FOCUS_BLOCK.get())
                {
                    if(b instanceof RegolithFilterBlock)
                    {
                        RegolithFilterEntity be=(RegolithFilterEntity)level.getBlockEntity(pos);
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
        return new RegolithFocusMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    public Component getDisplayName() 
    {
        return Component.translatable("container.arcanerefraction.regolith_focus_menu");
    }
}
