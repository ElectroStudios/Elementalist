package net.electro.elementalist.effect;

import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FrozenEffect extends MobEffect {
    public FrozenEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level().isClientSide()) {
            Vec3 position = Utility.getRandomVectorCube(pLivingEntity.getRandom(), pLivingEntity.getBoundingBox());
            pLivingEntity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
                    position.x, position.y, position.z, 0, 0, 0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
