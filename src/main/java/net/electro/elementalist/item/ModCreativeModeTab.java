package net.electro.elementalist.item;

import net.electro.elementalist.Elementalist;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            Elementalist.MOD_ID);
    public static RegistryObject<CreativeModeTab> ELEMENTALIST_TAB = CREATIVE_MODE_TABS.register("elementalist",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.FIRE_BRACELET_IRON.get()))
                    .title(Component.translatable("itemGroup.elementalist"))
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
