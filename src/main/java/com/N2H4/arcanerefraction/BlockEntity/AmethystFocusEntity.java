package com.N2H4.arcanerefraction.BlockEntity;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FOCUS_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FOCUS_ENTITY;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.DISPERSIVE_AMETHYST_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.RAY_PARTICLE;

import java.util.ArrayList;
import java.util.List;

import com.N2H4.arcanerefraction.Block.AmethystFocusBlock;
import com.N2H4.arcanerefraction.Block.DispersiveAmethysyBlock;
import com.N2H4.arcanerefraction.Menu.AmethystFocusMenu;
import com.N2H4.arcanerefraction.particle.RayParticle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.common.util.Lazy;



public class AmethystFocusEntity extends BlockEntity implements MenuProvider
{
    int timer = 300;
    int timer2 = 0;
    int ray_cooldown=120;
    boolean wokeUp = false;
    int lens_size = 0;
    boolean is_formed = false;
    boolean sky_access = false;
    List<BlockPos> processed_positions;

    private final Lazy<ItemStackHandler> optional = Lazy.of(() -> this.inventory);
    
    private final ItemStackHandler inventory = new ItemStackHandler(25)
    {
        @Override
        protected void onContentsChanged(int slot) 
        {
            super.onContentsChanged(slot);
            AmethystFocusEntity.this.setChanged();
        };

        public int getSlotLimit(int slot) 
        {
            return 1;
        };
    };
    
    public AmethystFocusEntity(BlockPos pos, BlockState state) 
    {
        super(AMETHYST_FOCUS_ENTITY.get(), pos, state);
        processed_positions = new ArrayList<BlockPos>();
    }

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
            if (timer > 20)
            {
                timer = 0;
                checkSkyAccess();
                scanUnder();
                this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
            if(timer2>10 && sky_access)
            {
                timer2=0;
                grow();
            }
        }
    }
    
    public void spawnParticles()
    {
        if(lens_size>=1)
        {
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 0, 0, 0);
        }
        if(lens_size>=2)
        {
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+1, worldPosition.getY(), worldPosition.getZ()+1, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-1, worldPosition.getY(), worldPosition.getZ()-1, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+1, worldPosition.getY(), worldPosition.getZ()-1, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-1, worldPosition.getY(), worldPosition.getZ()+1, 0, 0, 0);
        }
        if(lens_size>=3)
        {
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+2, worldPosition.getY(), worldPosition.getZ(), 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-2, worldPosition.getY(), worldPosition.getZ(), 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()+2, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()-2, 0, 0, 0);
        }
        if(lens_size>=4)
        {
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+2, worldPosition.getY(), worldPosition.getZ()+2, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-2, worldPosition.getY(), worldPosition.getZ()-2, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+2, worldPosition.getY(), worldPosition.getZ()-2, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-2, worldPosition.getY(), worldPosition.getZ()+2, 0, 0, 0);
        }
        if(lens_size==5)
        {
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+3, worldPosition.getY(), worldPosition.getZ()+3, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-3, worldPosition.getY(), worldPosition.getZ()-3, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+3, worldPosition.getY(), worldPosition.getZ()-3, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-3, worldPosition.getY(), worldPosition.getZ()+3, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()+5, worldPosition.getY(), worldPosition.getZ(), 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX()-5, worldPosition.getY(), worldPosition.getZ(), 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()+5, 0, 0, 0);
            level.addParticle((SimpleParticleType)RAY_PARTICLE.get(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()-5, 0, 0, 0);
        }
    }

    private void scanUnder()
    {
        processed_positions.clear();
        processed_positions.add(new BlockPos(worldPosition));
        int size = lens_size != 5 ? lens_size : 6;
        for(int i=-size;i<=size;i++)
        {
            for(int j=-size;j<=size;j++)
            {
                BlockPos pos=worldPosition.offset(i,0,j);
                if(level.getBlockState(pos).getBlock() == DISPERSIVE_AMETHYST_BLOCK.get() || level.getBlockState(pos).getBlock() == AMETHYST_FOCUS_BLOCK.get())
                {
                    for(int depth=1;depth<=10;depth++)
                    {
                        pos=worldPosition.offset(i,-depth,j);
                        if(level.getBlockState(pos).getBlock()!=Blocks.AIR && level.getBlockState(pos).getBlock()!=DISPERSIVE_AMETHYST_BLOCK.get() && level.getBlockState(pos).getBlock()!=AMETHYST_FOCUS_BLOCK.get())
                        {
                            processed_positions.add(pos);
                            break;
                        }
                    }
                } 
            }
        }
    }

    public void getUpdated()
    {
        is_formed=false;
    }

    public void interact()
    {
        lens_size=calcLensSize();
        checkSkyAccess();
        if(!is_formed && lens_size>0 && sky_access)
        {
            is_formed=true;
        }
    }

    private void hurtMobs() 
    {
        Vec3 topCorner = this.worldPosition.offset(5, 5, 5).getCenter();
        Vec3 bottomCorner = this.worldPosition.offset(-5, -5, -5).getCenter();
        AABB box = new AABB(topCorner, bottomCorner);
        /*Stream<BlockState> blockstates = this.level.getBlockStates(box);
        List<BlockState> list=new ArrayList<BlockState>();
        blockstates.forEach((x -> list.add(x)));
        for (BlockState target : list){
        if (target.getBlock() instanceof CropBlock)
            {
                //CropBlock c=(CropBlock)target.getBlock();
                //c.growCrops(this.level, this.worldPosition.offset(0,-3,0), target);
                level.scheduleTick(worldPosition.offset(0,-1,0), target.getBlock(), 0);
            }
        }*/
        BlockState b=this.level.getBlockState(worldPosition.offset(0,-2,0));
        if (b.getBlock() instanceof SugarCaneBlock)
        {
            SugarCaneBlock s=(SugarCaneBlock)b.getBlock();
            if(lens_size==1)
                s.randomTick(b, (ServerLevel)this.level, worldPosition.offset(0,-2,0), level.random);
        }
        
    }

    @SuppressWarnings("deprecation")
    private void grow()
    {
        for (BlockPos pos : processed_positions) 
        {
            BlockState bs=this.level.getBlockState(pos);
            Block b=bs.getBlock();
            if (isAllowedCrop(b) && (b instanceof IPlantable || b instanceof BonemealableBlock))
            {
                level.scheduleTick(pos, b, 1);
		        b.randomTick(bs, (ServerLevel)level, pos, level.random);
		        level.levelEvent(2005, pos, 1);

            }
        }
    }

    private boolean isAllowedCrop(Block b)
    {
        return b!=Blocks.GRASS_BLOCK && !(b instanceof DoublePlantBlock);
    }

    @Override
    public void onLoad() 
    {
        super.onLoad();
        wakeUpParts();
    }

    @Override
    public void load(CompoundTag nbt) 
    {
        super.load(nbt);
        CompoundTag data = nbt.getCompound(MODID);
        this.inventory.deserializeNBT(data.getCompound("Inventory"));
        this.is_formed=data.getBoolean("Formed");
        this.lens_size=data.getInt("Size");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) 
    {
        super.saveAdditional(nbt);
        var data=new CompoundTag();
        data.put("Inventory", this.inventory.serializeNBT());
        data.putBoolean("Formed", this.is_formed);
        data.putInt("Size", this.lens_size);
        nbt.put(MODID, data);
    }

    @Override
    public CompoundTag getUpdateTag() 
    {
        CompoundTag nbt=super.getUpdateTag();
        var data=new CompoundTag();
        data.putBoolean("Formed", this.is_formed);
        data.putBoolean("Sky", this.sky_access);
        data.putInt("Size", this.lens_size);
        nbt.put(MODID, data);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) 
    {
        CompoundTag data = tag.getCompound(MODID);
        this.is_formed=data.getBoolean("Formed");
        this.sky_access=data.getBoolean("Sky");
        this.lens_size=data.getInt("Size");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() 
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag().getCompound(MODID);
        this.is_formed=data.getBoolean("Formed");
        this.sky_access=data.getBoolean("Sky");
        this.lens_size=data.getInt("Size");
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public Lazy<ItemStackHandler> getOptional() {
        return this.optional;
    }

    private void wakeUpParts()
    {
        int size = lens_size != 5 ? lens_size : 6;
        for(int i=-size;i<=size;i++)
        {
            for(int j=-size;j<=size;j++)
            {
                if(!(i==0 && j==0))
                {
                    Block b=level.getBlockState(worldPosition.offset(i,0,j)).getBlock();
                    if(b instanceof DispersiveAmethysyBlock)
                        ((DispersiveAmethysyBlock)b).setMaster(this);
                }
            }
        }

    }

    private int calcLensSize()
    {
        int size=0;
        is_formed=false;

        //check if size 1
        
        for(int i=-1;i<=1;i++)
        {
            for(int j=-1;j<=1;j++)
            {
                int value=(i*i)+(j*j);
                if(value<=1 && value!=0)
                {
                    Block b=level.getBlockState(worldPosition.offset(i,0,j)).getBlock();
                    if(!(b instanceof DispersiveAmethysyBlock))
                        return size;
                    else
                        ((DispersiveAmethysyBlock)b).setMaster(this);
                }
            }
        }
        size=1;
        is_formed=true;

        //check size 2

        for(int i=-2;i<=2;i++)
        {
            for(int j=-2;j<=2;j++)
            {
                int value=(i*i)+(j*j);
                if(value<=5 && value>1 && value!=0)
                {
                    Block b=level.getBlockState(worldPosition.offset(i,0,j)).getBlock();
                    if(!(b instanceof DispersiveAmethysyBlock))
                        return size;
                    else
                        ((DispersiveAmethysyBlock)b).setMaster(this);
                }
            }
        }
        size=2;
        
        //check size 3

        for(int i=-3;i<=3;i++)
        {
            for(int j=-3;j<=3;j++)
            {
                int value=(i*i)+(j*j);
                if(value<=10 && value>5 && value!=0)
                {
                    Block b=level.getBlockState(worldPosition.offset(i,0,j)).getBlock();
                    if(!(b instanceof DispersiveAmethysyBlock))
                        return size;
                    else
                        ((DispersiveAmethysyBlock)b).setMaster(this);
                }
            }
        }
        size=3;

        //check size 4

        for(int i=-4;i<=4;i++)
        {
            for(int j=-4;j<=4;j++)
            {
                int value=(i*i)+(j*j);
                if(value<=20 && value>10 && value!=0)
                {
                    Block b=level.getBlockState(worldPosition.offset(i,0,j)).getBlock();
                    if(!(b instanceof DispersiveAmethysyBlock))
                        return size;
                    else
                        ((DispersiveAmethysyBlock)b).setMaster(this);
                }
            }
        }
        size=4;

        //check size 5

        for(int i=-6;i<=6;i++)
        {
            for(int j=-6;j<=6;j++)
            {
                int value=(i*i)+(j*j);
                if(value<=41 && value>20 && value!=0)
                {
                    Block b=level.getBlockState(worldPosition.offset(i,0,j)).getBlock();
                    if(!(b instanceof DispersiveAmethysyBlock))
                        return size;
                    else
                        ((DispersiveAmethysyBlock)b).setMaster(this);
                }
            }
        }
        size=5;

        return size;
    }

    void checkSkyAccess()
    {
        sky_access=true;
        int size = lens_size != 5 ? lens_size : 6;
        for(int i=-size;i<=size;i++)
        {
            for(int j=-size;j<=size;j++)
            {
                if(!(i==0 && j==0))
                {
                    BlockPos pos=worldPosition.offset(i,0,j);
                    if(level.getBlockState(pos).getBlock() instanceof DispersiveAmethysyBlock && !level.canSeeSky(pos.above()))
                    {
                        sky_access=false;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) 
    {
        return new AmethystFocusMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    public Component getDisplayName() 
    {
        return Component.translatable("container.arcanerefraction.amethyst_focus_menu");
    }
}
