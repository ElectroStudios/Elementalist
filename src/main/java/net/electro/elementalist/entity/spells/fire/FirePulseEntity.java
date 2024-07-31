package net.electro.elementalist.entity.spells.fire;

import net.electro.elementalist.registry.ParticleRegistry;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.entity.spells.MasterSpellEntity;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FirePulseEntity extends MasterSpellEntity {
    private int duration = 20;
    private final int DURATION_MAX = 20;
    private final float RADIUS = 15f;
    public FirePulseEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FirePulseEntity(LivingEntity owner, DamageType damageType) {
        super(EntityRegistry.FIRE_PULSE.get(), owner, damageType);
    }

    private void DealDamage() {
        DamageDealer dd = new DamageDealer(this.position(), getOwner(), this, this.ignoredEntities, this.damageType) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.setSecondsOnFire(damageType.ADDITIONAL_EFFECT_MULTIPLIER);
                ignoredEntities.add(entity);
                super.damageEffects(entity, effectAmount, direction);
            }
        };
        dd.dealDamageSphere((1 - (duration / (float) DURATION_MAX)) * RADIUS, 3);
    }

    @Override
    public void tick() {
        super.tick();
        duration--;
        RandomSource random = this.level().random;

        if (this.level().isClientSide()) {
            if (duration % 3 == 0) {
                for (int i = 0; i < 100; i++) {
                    double randomAngle = 2 * Math.PI * random.nextFloat();
                    float currentRadius = (1 - (duration / (float) DURATION_MAX)) * RADIUS;
                    this.level().addParticle(ParticleRegistry.FIRE_FLASH_PARTICLES.get(),
                            Math.sin(randomAngle) * currentRadius + this.getX(),
                            this.getY() + (3 * random.nextFloat()) - 2f,
                            Math.cos(randomAngle) * currentRadius + this.getZ(),
                            0, 0.25f, 0);
                }
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 2f, 1f, false);
            }
        }
        else {

            if (duration % 3 == 0) {
                DealDamage();
            }

            if (duration <= 0) {
                this.discard();
            }
        }
    }
}