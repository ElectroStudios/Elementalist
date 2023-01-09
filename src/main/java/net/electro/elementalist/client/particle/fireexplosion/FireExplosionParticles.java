package net.electro.elementalist.client.particle.fireexplosion;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class FireExplosionParticles extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected FireExplosionParticles(ClientLevel level, double xPos, double yPos, double zPos, SpriteSet sprites, double xd, double yd, double zd) {
        super(level, xPos, yPos, zPos, xd, yd, zd);
        RandomSource random  = this.level.random;
        this.sprites = sprites;
        this.friction = 0.8F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize = 0.4F + random.nextFloat() * 0.3F;
        this.lifetime = 15 + random.nextInt(10);
        this.setSpriteFromAge(sprites);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new FireExplosionParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
