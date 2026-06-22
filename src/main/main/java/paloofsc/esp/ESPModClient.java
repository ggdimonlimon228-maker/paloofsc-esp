package paloofsc.esp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import paloofsc.esp.config.ESPConfig;
import paloofsc.esp.gui.ESPGuiScreen;
import paloofsc.esp.render.ESPRenderer;

public class ESPModClient implements ClientModInitializer {

    public static ESPConfig config;
    public static ESPRenderer renderer;
    private static KeyBinding keyToggle;
    private static KeyBinding keyMenu;

    @Override
    public void onInitializeClient() {
        System.out.println("[PALOOFSC] ESP Injected — THE PESS SEES ALL");
        config = ESPConfig.load();
        renderer = new ESPRenderer();

        keyToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.paloofsc_esp.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "Paloofsc ESP"
        ));
        keyMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.paloofsc_esp.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Paloofsc ESP"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyToggle.wasPressed()) {
                config.espEnabled = !config.espEnabled;
                if (client.player != null)
                    client.player.sendMessage(Text.literal("§d[ESP] " + (config.espEnabled ? "§aON" : "§cOFF")), true);
            }
            if (keyMenu.wasPressed())
                MinecraftClient.getInstance().setScreen(new ESPGuiScreen());
        });

        WorldRenderEvents.LAST.register(context -> {
            if (config.espEnabled)
                renderer.render(context.matrixStack(), context.camera(), context.tickCounter().getTickDelta(true));
        });
    }
}
