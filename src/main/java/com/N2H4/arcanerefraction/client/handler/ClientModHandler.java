package com.N2H4.arcanerefraction.client.handler;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FILTER_MENU;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.AMETHYST_FOCUS_MENU;

import com.N2H4.arcanerefraction.ArcaneRefractionMod;
import com.N2H4.arcanerefraction.Menu.AmethystFocusMenu;
import com.N2H4.arcanerefraction.client.screen.AmethystFilterScreen;
import com.N2H4.arcanerefraction.client.screen.AmethystFocusScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod.EventBusSubscriber(modid=ArcaneRefractionMod.MODID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientModHandler 
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(AMETHYST_FOCUS_MENU.get(), AmethystFocusScreen::new);
            MenuScreens.register(AMETHYST_FILTER_MENU.get(), AmethystFilterScreen::new);
        });
    }
}
