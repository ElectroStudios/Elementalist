package net.electro.elementalist.util;

import net.electro.elementalist.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectUtil {
    public static void addEffect(MobEffect effect, int duration, int amplifier, LivingEntity entity) {
        if (effect == ModEffects.COLD.get()) {
            MobEffectInstance coldEffect = entity.getEffect(effect);
            MobEffectInstance wetEffect = entity.getEffect(ModEffects.WET.get());
            MobEffectInstance frozenEffect = entity.getEffect(ModEffects.FROZEN.get());
            if (wetEffect != null && wetEffect.getDuration() > 0) {
                entity.removeEffect(ModEffects.WET.get());
                entity.addEffect(new MobEffectInstance(ModEffects.FROZEN.get(), 100, 1, false, false, true));
            } else if (frozenEffect != null && frozenEffect.getDuration() > 0) {
            } else if (coldEffect != null && coldEffect.getDuration() > 0) {
                if (coldEffect.getAmplifier() + amplifier >= 10) {
                    entity.removeEffect(effect);
                    entity.addEffect(new MobEffectInstance(ModEffects.FROZEN.get(), 100, 1, false, false, true));
                }
                else {
                    entity.addEffect(new MobEffectInstance(effect, duration, coldEffect.getAmplifier() + amplifier, false, false, true));
                }
            }
            else {
                entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, true));
            }
        }
        else if (effect == ModEffects.WET.get()) {
            MobEffectInstance coldEffect = entity.getEffect(ModEffects.COLD.get());
            MobEffectInstance frozenEffect = entity.getEffect(ModEffects.FROZEN.get());
            if (coldEffect != null && coldEffect.getDuration() > 0) {
                entity.removeEffect(ModEffects.COLD.get());
                entity.addEffect(new MobEffectInstance(ModEffects.FROZEN.get(), 100, 1, false, false, true));
            } else if (frozenEffect != null && frozenEffect.getDuration() > 0) {
            } else {
                entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, true));
            }
        }
        else {
            entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, true));
        }
    }
}
