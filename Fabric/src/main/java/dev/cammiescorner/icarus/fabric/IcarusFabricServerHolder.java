package dev.cammiescorner.icarus.fabric;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

/**
 * Holds the active logical server so config save hooks can apply game rules
 */
public final class IcarusFabricServerHolder {

    private static volatile @Nullable MinecraftServer server;

    private IcarusFabricServerHolder() {
    }

    public static void set(MinecraftServer s) {
        server = s;
    }

    public static void clear() {
        server = null;
    }

    public static @Nullable MinecraftServer get() {
        return server;
    }
}
