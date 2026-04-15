package dev.cammiescorner.icarus.fabric.integration.jei;

import dev.cammiescorner.icarus.Icarus;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.Identifier;

@JeiPlugin
public final class IcarusJeiPlugin implements IModPlugin {
    private static final Identifier UID = Icarus.id("jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }
}
