package net.electro.elementalist.entity.projectiles;

import net.electro.elementalist.registry.EffectRegistry;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.SyncEffectsS2CPacket;
import net.electro.elementalist.util.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IceSpearProjectile extends MasterSpellProjectile implements IEffectSpell {
    public IceSpearProjectile(EntityType<? extends IceSpearProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IceSpearProjectile(LivingEntity owner, DamageType damageType) {
        super(EntityRegistry.ICE_SPEAR.get(), owner, 3, damageType);
    }

    @Override
    protected void projectileTickEffects() {
//        this.level().addParticle(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(),
//                0, 0, 0);
    }

    @Override
    public void explodeClient() {
        for (int i = 0; i < 50; i++) {
            Vec3 particlePos = Utility.getRandomVectorSphere(this.level().getRandom(), 2f, true).add(this.position());
            this.level().addParticle(ParticleTypes.CLOUD, true,
                    particlePos.x, particlePos.y, particlePos.z, 0f, 0.02f, 0f);
        }
        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL,
                3f, 1f, false);
    }

    @Override
    protected void onHit(HitResult pResult) {
            if (pResult.getType() == HitResult.Type.ENTITY) {
                if (((EntityHitResult)pResult).getEntity() instanceof LivingEntity hitEntity) {
                    if (!this.level().isClientSide()) {
                        EffectUtil.addEffect(EffectRegistry.COLD.get(), 100, 5, hitEntity);
                        MessageRegistry.sendToAllPlayers(new SyncEffectsS2CPacket(this.getId(), hitEntity.getId()));
                        hitEntity.knockback(damageType.KNOCKBACK, this.getForward().x, this.getForward().z);
                        hitEntity.hurt(this.damageSources().indirectMagic(this, this.getOwner()),
                                damageType.calculateDamage((LivingEntity) this.getOwner(), hitEntity));
                    }
                }
            }
        super.onHit(pResult);
    }

    @Override
    public void effectClient(LivingEntity target) {
        EffectUtil.addEffect(EffectRegistry.COLD.get(), 100, 5, target);
    }
}
