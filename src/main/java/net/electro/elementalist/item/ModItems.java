package net.electro.elementalist.item;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.item.bracelets.UnchargedStaff;
import net.electro.elementalist.util.Element;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Elementalist.MOD_ID);

    public static final RegistryObject<Item> FIRE_BRACELET_IRON = ITEMS.register("fire_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(1), Element.FIRE));

    public static final RegistryObject<Item> WATER_BRACELET_IRON = ITEMS.register("water_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(1), Element.WATER));

    public static final RegistryObject<Item> ICE_BRACELET_IRON = ITEMS.register("ice_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(1), Element.ICE));

    public static final RegistryObject<Item> ELEMENTALIST_GRIMOIRE = ITEMS.register("elementalist_grimoire",
            () -> new ElementalistGrimoire(new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(1)));

    public static final RegistryObject<Item> UNCHARGED_STAFF = ITEMS.register("uncharged_staff",
            () -> new UnchargedStaff(new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(1)));

    public static final RegistryObject<Item> WATER_SPIRIT_SPAWN_EGG = ITEMS.register("water_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WATER_SPIRIT, 0xAAAAAFF, 0xFFFFFF,
                    new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(64)));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
