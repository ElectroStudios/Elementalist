package net.electro.elementalist.entities.projectiles;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.util.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

public class FireballBasic extends MasterSpellProjectile {
    protected static final float EXPLOSION_RADIUS = 5f;
    public FireballBasic(EntityType<? extends FireballBasic> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireballBasic(LivingEntity owner, DamageType damageType) {
        super(ModEntities.FIREBALL_BASIC.get(), owner, 1, damageType);
    }

    @Override
    protected void projectileTickEffects() {
        this.level.addParticle(ModParticles.FIRE_EXPLOSION_PARTICLES.get(), this.getX(), this.getY(), this.getZ(),
                0, 0, 0);
    }

    @Override
    public void explodeClient() {
        for (int i = 0; i < 30; i++) {
            Vec3 particlePos = Utility.getRandomVectorSphere(this.level.getRandom(), 2f, true).add(this.position());
            this.level.addParticle(ModParticles.FIRE_EXPLOSION_PARTICLES.get(), true,
                    particlePos.x, particlePos.y, particlePos.z, 0f, 0f, 0f);
        }
        for (int i = 0; i < 20; i++) {
            Vec3 particleSpeed = Utility.getRandomVectorSphere(this.level.getRandom(), 1f, true);
            this.level.addParticle(ParticleTypes.SMOKE, true,
                    this.getX(), this.getY(), this.getZ(), particleSpeed.x, particleSpeed.y, particleSpeed.z);
        }
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL,
                3f, 1f, false);
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL,
                3f, 1f, false);
    }

    @Override
    public void explode() {
        if (!this.level.isClientSide()) {
            DamageDealer dd = new DamageDealer(this.position(), (LivingEntity) Objects.requireNonNull(this.getOwner()),
                    this, this.damageType) {
                @Override
                public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                    entity.setSecondsOnFire(damageType.ADDITIONAL_EFFECT_MULTIPLIER);
                    super.damageEffects(entity, effectAmount, direction);
                }
            };
            dd.dealDamageSphere(EXPLOSION_RADIUS, EXPLOSION_RADIUS);
            dd.setBlocksInRadius(1, 0.3f);
        }
        super.explode();
    }
}
