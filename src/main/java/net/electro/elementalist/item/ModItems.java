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
            () -> new ChargedStaff(new Item.Properties().stacksTo(1), Element.FIRE));

    public static final RegistryObject<Item> WATER_BRACELET_IRON = ITEMS.register("water_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().stacksTo(1), Element.WATER));

    public static final RegistryObject<Item> ICE_BRACELET_IRON = ITEMS.register("ice_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().stacksTo(1), Element.ICE));

    public static final RegistryObject<Item> LIGHTNING_BRACELET_IRON = ITEMS.register("lightning_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().stacksTo(1), Element.LIGHTNING));

    public static final RegistryObject<Item> AIR_BRACELET_IRON = ITEMS.register("air_bracelet_iron",
            () -> new ChargedStaff(new Item.Properties().stacksTo(1), Element.AIR));
    public static final RegistryObject<Item> ELEMENTALIST_GRIMOIRE = ITEMS.register("elementalist_grimoire",
            () -> new ElementalistGrimoire(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> UNCHARGED_STAFF = ITEMS.register("uncharged_staff",
            () -> new UnchargedStaff(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> WATER_SPIRIT_SPAWN_EGG = ITEMS.register("water_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WATER_SPIRIT, 0xAAAAAFF, 0xFFFFFF,
                    new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> LESSER_DEMON_SPAWN_EGG = ITEMS.register("lesser_demon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LESSER_DEMON, 0xFFAAAA, 0x111111,
                    new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> BAKENEKO_SPAWN_EGG = ITEMS.register("bakeneko_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BAKENEKO, 0x896955, 0xE5CAA0,
                    new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
