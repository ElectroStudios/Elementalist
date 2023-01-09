package net.electro.elementalist.client.particle;

import net.electro.elementalist.Elementalist;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Elementalist.MOD_ID);
    public static final RegistryObject<SimpleParticleType> FIRE_EXPLOSION_PARTICLES =
            PARTICLE_TYPES.register("fire_explosion", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> FIRE_FLASH_PARTICLES =
            PARTICLE_TYPES.register("fire_flash", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }
}
