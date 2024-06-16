package net.electro.elementalist;

import com.mojang.logging.LogUtils;
import net.electro.elementalist.block.ModBlocks;
import net.electro.elementalist.block.entity.ModBlockEntities;
import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.effect.ModEffects;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.item.ModCreativeModeTab;
import net.electro.elementalist.item.ModItems;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.spells.SpellRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
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

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModEntities.ENTITIES.register(modEventBus);

        ModParticles.register(modEventBus);

        ModCreativeModeTab.register(modEventBus);

        ModEffects.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        SpellRegistry.register(modEventBus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeModeTab.ELEMENTALIST_TAB.get()) {
            event.accept(ModItems.FIRE_BRACELET_IRON);
            event.accept(ModItems.WATER_BRACELET_IRON);
            event.accept(ModItems.AIR_BRACELET_IRON);
            event.accept(ModItems.LIGHTNING_BRACELET_IRON);
            event.accept(ModItems.ICE_BRACELET_IRON);
            event.accept(ModItems.ELEMENTALIST_GRIMOIRE);
            event.accept(ModItems.UNCHARGED_STAFF);
            event.accept(ModItems.WATER_SPIRIT_SPAWN_EGG);
            event.accept(ModItems.LESSER_DEMON_SPAWN_EGG);
            event.accept(ModItems.BAKENEKO_SPAWN_EGG);
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
