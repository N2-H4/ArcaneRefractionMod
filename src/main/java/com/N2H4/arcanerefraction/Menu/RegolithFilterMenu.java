package com.N2H4.arcanerefraction.Menu;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_FILTER_BLOCK;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_FILTER_MENU;

import org.jetbrains.annotations.NotNull;

import com.N2H4.arcanerefraction.BlockEntity.RegolithFilterEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RegolithFilterMenu extends AbstractContainerMenu {

    private final RegolithFilterEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    // Client Constructor
    public RegolithFilterMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(additionalData.readBlockPos()));
    }

    // Server Constructor
    public RegolithFilterMenu(int containerId, Inventory playerInv, BlockEntity blockEntity) {
        super(REGOLITH_FILTER_MENU.get(), containerId);
        if (blockEntity instanceof RegolithFilterEntity be) {
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

    private void createBlockEntityInventory(RegolithFilterEntity be) {

        ItemStackHandler inv = be.getOptional().get();
        super.addSlot(new FilterSlot(inv, 0, 61, 48));
        super.addSlot(new FilterSlot(inv, 1, 79, 48));
        super.addSlot(new FilterSlot(inv, 2, 97, 48));
        super.addSlot(new FilterSlot(inv, 3, 61, 66));
        super.addSlot(new FilterSlot(inv, 4, 79, 66));
        super.addSlot(new FilterSlot(inv, 5, 97, 66));
        super.addSlot(new FilterSlot(inv, 6, 61, 84));
        super.addSlot(new FilterSlot(inv, 7, 79, 84));
        super.addSlot(new FilterSlot(inv, 8, 97, 84));
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
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId < 36) {
			super.clicked(slotId, dragType, clickTypeIn, player);
			return;
		}
		if (clickTypeIn == ClickType.THROW)
			return;

		ItemStack held = getCarried();;
		if (clickTypeIn == ClickType.CLONE) {
			if (player.isCreative() && held.isEmpty()) {
				ItemStack stackInSlot = this.slots.get(slotId).getItem().copy();
				stackInSlot.setCount(stackInSlot.getMaxStackSize());
				setCarried(stackInSlot);
				return;
			}
			return;
		}

		ItemStack insert;
		if (held.isEmpty()) {
			insert = ItemStack.EMPTY;
		} else {
			insert = held.copy();
			insert.setCount(1);
		}
		this.slots.get(slotId).set(insert);
		getSlot(slotId).setChanged();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {

        if (pIndex < 36) {
			ItemStack stackToInsert = this.slots.get(pIndex).getItem();
			for (int i = 36; i < 45; i++) {
				ItemStack stack = this.slots.get(i).getItem();
				if (stack.isEmpty()) {
					ItemStack copy = stackToInsert.copy();
					copy.setCount(1);
					this.slots.get(i).set(copy);
					getSlot(i).setChanged();
					break;
				}
			}
		} else {
            this.slots.get(pIndex).set(ItemStack.EMPTY);
			getSlot(pIndex).setChanged();
		}
		return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, REGOLITH_FILTER_BLOCK.get());
    }

    public RegolithFilterEntity getBlockEntity() {
        return this.blockEntity;
    }

    class FilterSlot extends SlotItemHandler {
        public FilterSlot(IItemHandler pContainer, int pContainerIndex, int pXPosition, int pYPosition) {
            super(pContainer, pContainerIndex, pXPosition, pYPosition);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return true;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void set(ItemStack stack) {
            super.set(stack);
        }
    }

}
