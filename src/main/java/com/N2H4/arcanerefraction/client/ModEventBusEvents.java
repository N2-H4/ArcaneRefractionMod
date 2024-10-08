package com.N2H4.arcanerefraction.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.common.Mod;

import static com.N2H4.arcanerefraction.ArcaneRefractionMod.RAY_PARTICLE;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.REGOLITH_PARTICLE;
import static com.N2H4.arcanerefraction.ArcaneRefractionMod.TEPHRA_PARTICLE;

import com.N2H4.arcanerefraction.ArcaneRefractionMod;
import com.N2H4.arcanerefraction.particle.RayParticle;
import com.N2H4.arcanerefraction.particle.RegolithParticle;
import com.N2H4.arcanerefraction.particle.TephraParticle;

@Mod.EventBusSubscriber(modid = ArcaneRefractionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("deprecation")
public class ModEventBusEvents
{
    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(RAY_PARTICLE.get(), RayParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(REGOLITH_PARTICLE.get(), RegolithParticle.RegolithProvider::new);
        Minecraft.getInstance().particleEngine.register(TEPHRA_PARTICLE.get(), TephraParticle.TephraProvider::new);
    }
}
