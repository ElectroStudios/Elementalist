package net.electro.elementalist.entities.spells.water;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.spells.SpellMasterEntity;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.ParticleUtil;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WaterStreamEntity extends SpellMasterEntity {
    private int duration = 12;
    public WaterStreamEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WaterStreamEntity(LivingEntity owner, DamageType damageType) {
        super(ModEntities.WATER_STREAM.get(), owner, damageType);
        this.setPos(this.position().add(0f, 0f, 0f).add(this.getForward().multiply(2.5f, 1f, 2.5f)));
    }




    private void DealDamage() {
        DamageDealer dd = new DamageDealer(this.position(), getOwner(), this, this.ignoredEntities) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.hurt(DamageSource.indirectMagic(SOURCE, OWNER), damageType.BASE_DAMAGE);
                entity.knockback(damageType.KNOCKBACK, direction.x, direction.z);
            }
        };
        dd.dealDamageTube(3, 15, this.getForward());
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (duration == 8) {
                DealDamage();
            }
            if (duration <= 0) {
                this.discard();
            }
        }
        else {
            if (duration == 12) {
                this.level.playLocalSound(this.position().x, this.position().y, this.position().z, SoundEvents.PLAYER_SPLASH_HIGH_SPEED,
                        SoundSource.NEUTRAL, 2f, 1f, false);
                for (int i = 0; i < 100; i++) {
                    Vec3 particlePos = Utility.getRandomVectorSphere(this.level.random, 3, true).add(this.position());
                    Vec3 particleSpeed = Utility.multiplyVec3ByFloat(this.getForward(), 5);
                    this.level.addParticle(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z,
                            particleSpeed.x, particleSpeed.y, particleSpeed.z);
                }
            }
        }
        duration--;

    }
}