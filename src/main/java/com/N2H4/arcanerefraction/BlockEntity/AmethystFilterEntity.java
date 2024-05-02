package com.N2H4.arcanerefraction.BlockEntity;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FILTER_ENTITY;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.MODID;

import com.N2H4.arcanerefraction.Menu.AmethystFilterMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AmethystFilterEntity extends BlockEntity implements MenuProvider 
{
    protected final Lazy<ItemStackHandler> optional = Lazy.of(() -> this.inventory);

    protected final ItemStackHandler inventory = new ItemStackHandler(9)
    {
        @Override
        protected void onContentsChanged(int slot) 
        {
            super.onContentsChanged(slot);
            AmethystFilterEntity.this.setChanged();
        };

        public int getSlotLimit(int slot) 
        {
            return 1;
        };
    };

    public AmethystFilterEntity(BlockPos pos, BlockState state) 
    {
        super(AMETHYST_FILTER_ENTITY.get(), pos, state);
    }

    protected AmethystFilterEntity(BlockPos pos, BlockState state, BlockEntityType<?> type)
    {
        super(type, pos, state);
    }

    @Override
    public void onLoad() 
    {
        super.onLoad();
    }

    @Override
    public void load(CompoundTag nbt) 
    {
        super.load(nbt);
        CompoundTag data = nbt.getCompound(MODID);
        this.inventory.deserializeNBT(data.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) 
    {
        super.saveAdditional(nbt);
        var data=new CompoundTag();
        data.put("Inventory", this.inventory.serializeNBT());
        nbt.put(MODID, data);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() 
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AmethystFilterMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.arcanerefraction.amethyst_filter_menu");
    }

    public Lazy<ItemStackHandler> getOptional() {
        return this.optional;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }
    
}
