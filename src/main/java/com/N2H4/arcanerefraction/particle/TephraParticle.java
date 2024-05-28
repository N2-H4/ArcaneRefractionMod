package com.N2H4.arcanerefraction.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TephraParticle extends RayParticle
{

    protected TephraParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) 
    {
        super(pLevel, pX, pY, pZ, spriteSet, pXSpeed, pYSpeed, pZSpeed);
        this.rCol = 0.749f;
        this.gCol = 0.537f;
        this.bCol = 0.317f;
    }

    @OnlyIn(Dist.CLIENT)
    public static class TephraProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public TephraProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new TephraParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}