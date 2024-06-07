package com.N2H4.arcanerefraction.Item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.WritableBookItem;

import com.N2H4.arcanerefraction.client.screen.ARGuideScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.level.Level;

public class ARGuide extends Item {

    public ARGuide(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide)
            return InteractionResultHolder.success(itemstack);
        Minecraft.getInstance().setScreen(new ARGuideScreen());
        return InteractionResultHolder.success(itemstack);
    }

}
