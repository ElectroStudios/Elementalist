package net.electro.elementalist.effect;

import net.electro.elementalist.Elementalist;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            Elementalist.MOD_ID);

    public static final RegistryObject<MobEffect> COLD = MOB_EFFECTS.register("cold",
            () -> new ColdEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "5034323e-65a3-4020-8b95-40b1312fec98",
                            -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> ELECTRIFIED = MOB_EFFECTS.register("electrified",
            () -> new ElectrifiedEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "4cecdb33-d129-4d1b-a9c0-b14721d01537",
                            -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> FROZEN = MOB_EFFECTS.register("frozen",
            () -> new FrozenEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "416d3933-31a5-44fd-af01-9036d62d7b32",
                            -10, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> WET = MOB_EFFECTS.register("wet",
            () -> new WetEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
