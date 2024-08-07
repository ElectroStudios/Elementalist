package net.electro.elementalist.block.entity;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.registry.BlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Elementalist.MOD_ID);

    public static final RegistryObject<BlockEntityType<AltarBlockEntity>> ALTAR = BLOCK_ENTITIES.register("altar", () ->
            BlockEntityType.Builder.of(AltarBlockEntity::new, BlockRegistry.ALTAR.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
