package net.electro.elementalist.client.keybinding.keyaction;


import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;

public class KeyActionBase {
    protected static final Minecraft mc = Minecraft.getInstance();
    public KeyMapping keyMapping;

    public void trigger(InputEvent.Key event) {}
}
