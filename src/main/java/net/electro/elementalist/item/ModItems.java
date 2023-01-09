package net.electro.elementalist.item;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.item.bracelets.FireBracelet;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Elementalist.MOD_ID);

    public static final RegistryObject<Item> FIRE_BRACELET_IRON = ITEMS.register("fire_bracelet_iron",
            () -> new FireBracelet(new Item.Properties().tab(ModCreativeModeTab.ELEMENTALIST_TAB).stacksTo(1)));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
