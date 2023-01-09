package net.electro.elementalist.entities.projectiles;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.entities.ModEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public abstract class MasterSpellProjectile extends Projectile {
    protected static final float SPEED = 1.5f;
    public MasterSpellProjectile(EntityType<? extends MasterSpellProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public MasterSpellProjectile(LivingEntity owner) {
        super(ModEntities.FIREBALL_BASIC.get(), owner.level);
        setOwner(owner);
        setPos(owner.getEyePosition()
                .add(new Vec3(0, -0.3F, 0))
                .add(Vec3.directionFromRotation(owner.getRotationVector()).multiply(1, 1, 1)));
        this.shootProjectileFromRotation(owner.getXRot(), owner.getYRot(), 0.0F, SPEED, 1.0F);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        explosionEffects();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        explosionEffects();
    }

    protected void projectileTickEffects() {
    }
    @Override
    public void tick() {
        if (!this.level.isClientSide()) {
            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
        }
        Vec3 vec31 = this.getDeltaMovement();
        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
        if (this.level.isClientSide()) {
            this.projectileTickEffects();
        }
        super.tick();
    }

    protected void explosionEffects() {
    }

    public void shootProjectileFromRotation(float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
    }

    protected void explosionParticles(ParticleOptions particleOptions, float radius, int particleAmount)
    {
        for (int i = 0; i < particleAmount; i++) {
            RandomSource random = this.level.random;
            Vec3 randomVelocity = Vec3.directionFromRotation(new Vec2(((random.nextFloat() * 2) - 1) * 90, ((random.nextFloat() * 2) - 1) * 180));
            ((ServerLevel)this.level).sendParticles(particleOptions, this.getX(), this.getY(), this.getZ(),1, randomVelocity.x, randomVelocity.y, randomVelocity.z, radius);
        }
    }

    @Override
    protected void defineSynchedData() {
    }
}
