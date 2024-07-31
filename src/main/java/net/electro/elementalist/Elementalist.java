package net.electro.elementalist;

import com.mojang.logging.LogUtils;
import net.electro.elementalist.registry.BlockRegistry;
import net.electro.elementalist.block.entity.ModBlockEntities;
import net.electro.elementalist.registry.ParticleRegistry;
import net.electro.elementalist.registry.EffectRegistry;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.item.ModCreativeModeTab;
import net.electro.elementalist.registry.ItemRegistry;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.registry.SpellRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Elementalist.MOD_ID)
public class Elementalist
{
    public static final String MOD_ID = "elementalist";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Elementalist()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.register(modEventBus);

        BlockRegistry.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        EntityRegistry.ENTITIES.register(modEventBus);

        ParticleRegistry.register(modEventBus);

        ModCreativeModeTab.register(modEventBus);

        EffectRegistry.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        SpellRegistry.register(modEventBus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        MessageRegistry.register();
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeModeTab.ELEMENTALIST_TAB.get()) {
            event.accept(ItemRegistry.FIRE_BRACELET_IRON);
            event.accept(ItemRegistry.WATER_BRACELET_IRON);
            event.accept(ItemRegistry.AIR_BRACELET_IRON);
            event.accept(ItemRegistry.LIGHTNING_BRACELET_IRON);
            event.accept(ItemRegistry.ICE_BRACELET_IRON);
            event.accept(ItemRegistry.ELEMENTALIST_GRIMOIRE);
            event.accept(ItemRegistry.UNCHARGED_STAFF);
            event.accept(ItemRegistry.WATER_SPIRIT_SPAWN_EGG);
            event.accept(ItemRegistry.LESSER_DEMON_SPAWN_EGG);
            event.accept(ItemRegistry.BAKENEKO_SPAWN_EGG);
        }
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

}
