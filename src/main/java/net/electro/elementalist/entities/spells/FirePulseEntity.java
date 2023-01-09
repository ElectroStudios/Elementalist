package net.electro.elementalist.entities.spells;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.util.DamageDealer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FirePulseEntity extends SpellMasterEntity {
    private int duration = 20;
    private final int DURATION_MAX = 20;
    private final float RADIUS = 15f;
    private DamageDealer dd;
    private RandomSource random;
    public FirePulseEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FirePulseEntity(LivingEntity owner, float baseDamage) {
        super(ModEntities.FIRE_PULSE.get(), owner);
        random = owner.level.random;
        dd = new DamageDealer(this.position(), owner, this) {
            @Override
            public void DamageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.hurt(DamageSource.indirectMagic(SOURCE, OWNER), baseDamage);
                entity.setSecondsOnFire(5);
                entity.knockback(1.5f, direction.x, direction.z);
                ignoredEntities.add(entity);
            }
        };
    }

    @Override
    public void tick() {
        super.tick();
        duration--;
        random = this.level.random;

        if (this.level.isClientSide()) {
            if (duration % 3 == 0) {
                for (int i = 0; i < 100; i++) {
                    double randomAngle = 2 * Math.PI * random.nextFloat();
                    float currentRadius = (1 - (duration / (float) DURATION_MAX)) * RADIUS;
                    this.level.addParticle(ModParticles.FIRE_FLASH_PARTICLES.get(),
                            Math.sin(randomAngle) * currentRadius + this.getX(),
                            this.getY() + (2 * random.nextFloat()),
                            Math.cos(randomAngle) * currentRadius + this.getZ(),
                            0, 0.25f, 0);
                }
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 2f, 1f, false);
            }
        }
        else {

            if (duration % 3 == 0) {
                dd.DealDamageSphere((1 - (duration / (float) DURATION_MAX)) * RADIUS);
            }

            if (duration <= 0) {
                this.discard();
            }
        }
    }
}