package com.N2H4.arcanerefraction.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class RegolithParticle extends RayParticle
{

    protected RegolithParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) 
    {
        super(pLevel, pX, pY, pZ, spriteSet, pXSpeed, pYSpeed, pZSpeed);
        this.rCol = 0.61f;
        this.gCol = 0.84f;
        this.bCol = 0.8f;
    }

    @OnlyIn(Dist.CLIENT)
    public static class RegolithProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public RegolithProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RegolithParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}