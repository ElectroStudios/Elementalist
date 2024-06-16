package net.electro.elementalist.effect;

import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WetEffect extends MobEffect {
    protected WetEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level().isClientSide()) {
            if (pLivingEntity.tickCount % Math.max(1, 10 - pAmplifier) == 0) {
                Vec3 position = Utility.getRandomVectorCube(pLivingEntity.getRandom(), pLivingEntity.getBoundingBox());
                pLivingEntity.level().addParticle(ParticleTypes.DRIPPING_WATER, position.x, position.y, position.z, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
