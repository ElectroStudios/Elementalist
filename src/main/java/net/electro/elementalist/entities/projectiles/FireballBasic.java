package net.electro.elementalist.entities.projectiles;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.util.DamageDealer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class FireballBasic extends MasterSpellProjectile {
    protected static final float SPEED = 1.5f;
    protected static final float BASE_DAMAGE = 8f;
    protected static final float EXPLOSION_RADIUS = 5f;
    protected static final float KNOCKBACK_AMOUNT = 1.5f;
    public FireballBasic(EntityType<? extends FireballBasic> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireballBasic(LivingEntity owner) {
        super(owner);
    }

    @Override
    protected void projectileTickEffects() {
        this.level.addParticle(ModParticles.FIRE_EXPLOSION_PARTICLES.get(), this.getX(), this.getY(), this.getZ(),
                0, 0, 0);
    }

    @Override
    protected void explosionEffects() {
        ((ServerLevel)this.level).sendParticles(ModParticles.FIRE_EXPLOSION_PARTICLES.get(), this.getX(), this.getY(), this.getZ(), 20, 1F, 1F, 1F, 0F);
        ((ServerLevel)this.level).sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 100, 0.1F, 0.1F, 0.1F, 0.2F);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, 1.0F);
        this.playSound(SoundEvents.FIRECHARGE_USE, 4.0F, 1.0F);
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (!this.level.isClientSide()) {
            DamageDealer dd = new DamageDealer(this.position(), (LivingEntity) Objects.requireNonNull(this.getOwner()), this) {
                @Override
                public void DamageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                    entity.hurt(DamageSource.indirectMagic(SOURCE, OWNER), BASE_DAMAGE * effectAmount);
                    entity.setSecondsOnFire(3);
                    entity.knockback(effectAmount * KNOCKBACK_AMOUNT, direction.x, direction.z);
                }
            };
            dd.DealDamageSphere(EXPLOSION_RADIUS);
        }
        super.onHit(pResult);
    }
}
