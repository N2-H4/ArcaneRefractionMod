package com.N2H4.arcanerefraction.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.common.Mod;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.RAY_PARTICLE;

import com.N2H4.arcanerefraction.ArcaneRefractionMod;
import com.N2H4.arcanerefraction.particle.RayParticle;

@Mod.EventBusSubscriber(modid = ArcaneRefractionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("deprecation")
public class ModEventBusEvents
{
    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(RAY_PARTICLE.get(), RayParticle.Provider::new);
    }
}
