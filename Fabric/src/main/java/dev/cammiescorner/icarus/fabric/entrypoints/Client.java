package dev.cammiescorner.icarus.fabric.entrypoints;

import com.mojang.blaze3d.platform.InputConstants;
import dev.cammiescorner.icarus.client.IcarusClient;
import dev.cammiescorner.icarus.client.renderers.WingsLayer;
import dev.cammiescorner.icarus.Icarus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EntityType;
import org.lwjgl.glfw.GLFW;

public class Client implements ClientModInitializer {
    private static final KeyMapping.Category ICARUS_CATEGORY = KeyMapping.Category.register(Icarus.id("controls"));
    private static final KeyMapping TOGGLE_FORWARD_LOCK = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "key.icarus.toggle_forward_lock",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        ICARUS_CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType == EntityType.PLAYER) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                var wingLayer = new WingsLayer((RenderLayerParent) entityRenderer, context.getModelSet());
                registrationHelper.register(wingLayer);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_FORWARD_LOCK.consumeClick()) {
                boolean enabled = !IcarusClient.isForwardLockEnabled();
                IcarusClient.setForwardLockEnabled(enabled);
                if (client.player != null) {
                    client.player.sendSystemMessage(Component.literal("Icarus forward lock: " + (enabled ? "ON" : "OFF")));
                }
            }
        });
    }
}
