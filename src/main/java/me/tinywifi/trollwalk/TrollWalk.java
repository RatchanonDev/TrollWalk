package me.tinywifi.trollwalk;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrollWalk implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    public static boolean enabled = false;
    private static KeyBinding keyBinding;

    @Override
    public void onInitialize() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.trollwalk.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.trollwalk.main"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                 enabled = !enabled;
                assert client.player != null;
                client.player.sendMessage(Text.of("TrollWalk " + (enabled ? "enabled" : "disabled")), false);
             }
        });
    }
}