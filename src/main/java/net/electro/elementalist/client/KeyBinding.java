package net.electro.elementalist.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_ELEMENTALIST = "key.category.elementalist.elementalist";
    public static final String KEY_SPELL_SELECT = "key.elementalist.select_spell";
    public static final String KEY_ALTERNATE_SPELLS = "key.elementalist.alternate_spells";

    public static final KeyMapping SPELL_SELECT_KEY = new KeyMapping(KEY_SPELL_SELECT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_ELEMENTALIST);

    public static final KeyMapping ALTERNATE_SPELLS_KEY = new KeyMapping(KEY_ALTERNATE_SPELLS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, KEY_CATEGORY_ELEMENTALIST);
}
