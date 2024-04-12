package com.N2H4.arcanerefraction.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class RayParticle extends TextureSheetParticle
{
    private final Quaternionf rotation = new Quaternionf();
    private float x_rot=0;
    private float z_rot=0;

    protected RayParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) 
    {
        super(pLevel, pX, pY, pZ, 0, 0, 0);
        this.friction = 0F;
        this.quadSize *= 5F;
        this.lifetime = (int) ((Math.random() * (160 - 100)) + 100);
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics=false;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

        x_rot=(float)((Math.random()-0.5f)*Mth.HALF_PI)/2.0f;
        z_rot=(float)((Math.random()-0.5f)*Mth.HALF_PI)/2.0f;
    }

    @Override
    public ParticleRenderType getRenderType() 
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        fadeOut();
        super.tick();
    }

    private void fadeOut() 
    {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float f = (float)(this.x - vec3.x());
        float f1 = (float)(this.y - vec3.y());
        float f2 = (float)(this.z - vec3.z());
        //this.getFacingCameraMode().setRotation(this.rotation, pRenderInfo, 1);
        this.rotation.set(0, pRenderInfo.rotation().y(), 0, pRenderInfo.rotation().w());
        /*if (this.roll != 0.0F) {
            this.rotation.rotateZ(Mth.lerp(pPartialTicks, this.oRoll, this.roll));
        }*/
        this.rotation.rotateZ(-Mth.HALF_PI);
        this.rotation.rotateX(x_rot);
        this.rotation.rotateZ(z_rot);

        Vector3f[] avector3f = new Vector3f[]{
            new Vector3f(0.0F, -0.8F, 0.0F), new Vector3f(0.0F, 0.8F, 0.0F), new Vector3f(10.0F, 0.8F, 0.0F), new Vector3f(10.0F, -0.8F, 0.0F)
        };
        float f3 = this.getQuadSize(pPartialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(this.rotation);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(pPartialTicks);
        pBuffer.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z())
            .uv(f7, f5)
            .color(this.rCol, this.gCol, this.bCol, this.alpha)
            .uv2(j)
            .endVertex();
        pBuffer.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z())
            .uv(f7, f4)
            .color(this.rCol, this.gCol, this.bCol, this.alpha)
            .uv2(j)
            .endVertex();
        pBuffer.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z())
            .uv(f6, f4)
            .color(this.rCol, this.gCol, this.bCol, this.alpha)
            .uv2(j)
            .endVertex();
        pBuffer.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z())
            .uv(f6, f5)
            .color(this.rCol, this.gCol, this.bCol, this.alpha)
            .uv2(j)
            .endVertex();
    }
    

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RayParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
    
}