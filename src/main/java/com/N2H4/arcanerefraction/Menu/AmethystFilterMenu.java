package com.N2H4.arcanerefraction.Menu;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FILTER_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FILTER_MENU;

import org.jetbrains.annotations.NotNull;

import com.N2H4.arcanerefraction.BlockEntity.AmethystFilterEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AmethystFilterMenu extends AbstractContainerMenu 
{

    private final AmethystFilterEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    // Client Constructor
    public AmethystFilterMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(additionalData.readBlockPos()));
    }
    // Server Constructor
    public AmethystFilterMenu(int containerId, Inventory playerInv, BlockEntity blockEntity) {
        super(AMETHYST_FILTER_MENU.get(), containerId);
        if(blockEntity instanceof AmethystFilterEntity be) {
            this.blockEntity = be;
        } else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into menu"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);
        createBlockEntityInventory(be);
    }

    private void createBlockEntityInventory(AmethystFilterEntity be) {

        ItemStackHandler inv=be.getOptional().get();
        super.addSlot(new FocusSlot(inv,0,80,11));

        super.addSlot(new FocusSlot(inv,1,62,34));
        super.addSlot(new FocusSlot(inv,2,80,34));
        super.addSlot(new FocusSlot(inv,3,98,34));

        super.addSlot(new FocusSlot(inv,4,44,57));
        super.addSlot(new FocusSlot(inv,5,62,57));
        super.addSlot(new FocusSlot(inv,6,80,57));
        super.addSlot(new FocusSlot(inv,7,98,57));
        super.addSlot(new FocusSlot(inv,8,115,57));

        super.addSlot(new FocusSlot(inv,9,26,79));
        super.addSlot(new FocusSlot(inv,10,44,79));
        super.addSlot(new FocusSlot(inv,11,62,79));
        super.addSlot(new FocusSlot(inv,12,80,79));
        super.addSlot(new FocusSlot(inv,13,98,79));
        super.addSlot(new FocusSlot(inv,14,115,79));
        super.addSlot(new FocusSlot(inv,15,134,79));

        super.addSlot(new FocusSlot(inv,16,8,102));
        super.addSlot(new FocusSlot(inv,17,26,102));
        super.addSlot(new FocusSlot(inv,18,44,102));
        super.addSlot(new FocusSlot(inv,19,62,102));
        super.addSlot(new FocusSlot(inv,20,80,102));
        super.addSlot(new FocusSlot(inv,21,98,102));
        super.addSlot(new FocusSlot(inv,22,116,102));
        super.addSlot(new FocusSlot(inv,23,134,102));
        super.addSlot(new FocusSlot(inv,24,152,102));

    }

    private void createPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv,
                        9 + column + (row * 9),
                        8 + (column * 18),
                        140 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv,
                    column,
                    8 + (column * 18),
                    198));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if(fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if(!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if(pIndex < 36) {
            // We are inside of the player's inventory
            if(!moveItemStackTo(fromStack, 36, 61, false))
                return ItemStack.EMPTY;
        } else if (pIndex < 61) {
            // We are inside of the block entity inventory
            if(!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + pIndex);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(pPlayer, fromStack);

        return copyFromStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, AMETHYST_FILTER_BLOCK.get());
    }

    public AmethystFilterEntity getBlockEntity() {
        return this.blockEntity;
    }

    class FocusSlot extends SlotItemHandler {
        public FocusSlot(IItemHandler pContainer, int pContainerIndex, int pXPosition, int pYPosition) {
            super(pContainer, pContainerIndex, pXPosition, pYPosition);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return true;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
    
}
