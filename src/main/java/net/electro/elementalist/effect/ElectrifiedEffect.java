package net.electro.elementalist.effect;

import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ElectrifiedEffect extends MobEffect {
    protected ElectrifiedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity.tickCount % 10 == 0) {
                pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1f);
            }
        } else {
            for (int i = 0; i < pAmplifier; i++) {
                Vec3 position = Utility.getRandomVectorCube(pLivingEntity.getRandom(), pLivingEntity.getBoundingBox());
                pLivingEntity.level().addParticle(ParticleTypes.END_ROD, position.x, position.y, position.z, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
