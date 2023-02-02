package net.electro.elementalist.entities.spells.water;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.spells.SpellMasterEntity;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class WaterSlashEntity extends SpellMasterEntity {
    private int duration = 10;
    public WaterSlashEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WaterSlashEntity(LivingEntity owner, DamageType damageType) {
        super(ModEntities.WATER_SLASH.get(), owner, damageType);
        this.setPos(this.position().add(0f, -0.5f, 0f).add(this.getForward().multiply(2.5f, 1f, 2.5f)));
    }




    private void DealDamage() {
        List<LivingEntity> foundEntities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), (entity -> {
            return !entity.isSpectator() && entity.isPickable();
        }));
        foundEntities.removeAll(this.ignoredEntities);
        DamageDealer dd = new DamageDealer(this.position(), getOwner(), this, this.ignoredEntities) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.hurt(DamageSource.indirectMagic(SOURCE, OWNER), damageType.BASE_DAMAGE);
                entity.knockback(damageType.KNOCKBACK, direction.x, direction.z);
            }
        };
        for (LivingEntity entity : foundEntities) {
            dd.damageEffects(entity, 1, this.getForward().yRot(-90));
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (duration == 7) {
                DealDamage();
            }
            if (duration <= 0) {
                this.discard();
            }
        }
        else {
            if (duration == 10) {
                this.level.playLocalSound(this.position().x, this.position().y, this.position().z, SoundEvents.PLAYER_SPLASH_HIGH_SPEED,
                        SoundSource.NEUTRAL, 2f, 1f, false);
                for (int i = 0; i < 30; i++) {
                    Vec3 particlePos = Utility.getRandomVectorCube(this.level.random, this.getBoundingBox());
                    this.level.addParticle(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
                }
            }
        }
        duration--;

    }
}