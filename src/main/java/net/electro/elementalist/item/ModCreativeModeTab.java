package net.electro.elementalist.item;

import net.electro.elementalist.Elementalist;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModCreativeModeTab {
    public static final CreativeModeTab ELEMENTALIST_TAB = new CreativeModeTab("elementalist") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FIRE_BRACELET_IRON.get());
        }
    };
}
