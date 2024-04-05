package com.example.examplemod.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.Block.DispersiveAmethysyBlock;


import static com.example.examplemod.ExampleMod.TEST;

public class TestBlockEntity extends BlockEntity 
{
    int timer = 0;
    int size_timer = 0;
    boolean is_active = true;
    int lens_size = 0;
    boolean time_to_update = false;
    boolean is_formed = false;
    boolean sky_access = false;
    boolean is_redstone=false;
    
    public TestBlockEntity(BlockPos pos, BlockState state) 
    {
        super(TEST.get(), pos, state);
    }

    public void tickServer() 
    {
        if (!level.isClientSide() && is_active){
            timer++;
            size_timer++;
            if(time_to_update)
            {
                time_to_update=false;
            }
            if (timer > 100){
                timer = 0;
                if(getBlockState().getValue(BlockStateProperties.POWERED))
                    System.out.println("IS POWERED!");
                // only do this once per second
                //hurtMobs();
            }
        }
    }

    public void getUpdated()
    {
        is_formed=false;
        time_to_update=true;
    }

    public void interact()
    {
        if(!is_formed)
        {
            lens_size=calcLensSize();
            checkSkyAccess();
            is_formed=true;
        }
        else
        {
            //open menu here
        }
    }

    public void change_redstone(boolean state)
    {
        is_redstone=state;
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
}
