package net.electro.elementalist.entities.projectiles;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class IceSpear extends MasterSpellProjectile {
    public IceSpear(EntityType<? extends IceSpear> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IceSpear(LivingEntity owner, DamageType damageType) {
        super(ModEntities.ICE_SPEAR.get(), owner, 3, damageType);
    }

    @Override
    protected void projectileTickEffects() {
        this.level.addParticle(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(),
                0, 0, 0);
    }

    @Override
    public void explodeClient() {
        for (int i = 0; i < 50; i++) {
            Vec3 particlePos = Utility.getRandomVectorSphere(this.level.getRandom(), 2f, true).add(this.position());
            this.level.addParticle(ParticleTypes.CLOUD, true,
                    particlePos.x, particlePos.y, particlePos.z, 0f, 0.02f, 0f);
        }
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL,
                3f, 1f, false);
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (!this.level.isClientSide()) {
            if (pResult.getType() == HitResult.Type.ENTITY) {
                if (((EntityHitResult)pResult).getEntity() instanceof LivingEntity hitEntity) {
                    hitEntity.knockback(damageType.KNOCKBACK, this.getForward().x, this.getForward().z);
                    hitEntity.hurt(DamageSource.indirectMagic(this, this.getOwner()), damageType.BASE_DAMAGE);
                }
            }
        }
        super.onHit(pResult);
    }
}
