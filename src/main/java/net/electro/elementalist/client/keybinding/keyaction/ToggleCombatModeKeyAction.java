package net.electro.elementalist.client.keybinding.keyaction;

import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.client.keybinding.KeyBindingRegistry;
import net.electro.elementalist.networking.ToggleCombatModeC2SPacket;
import net.electro.elementalist.registry.MessageRegistry;
import net.minecraftforge.client.event.InputEvent;

public class ToggleCombatModeKeyAction extends KeyActionBase {
    public ToggleCombatModeKeyAction() {
        this.keyMapping = KeyBindingRegistry.TOGGLE_COMBAT_MODE_KEY;
    }

    @Override
    public void trigger(InputEvent.Key event) {
        if (event.getAction() == 1) {
            MessageRegistry.sendToServer(new ToggleCombatModeC2SPacket());
            ClientSpellStateData.toggleCombatMode();
        }
    }
}
