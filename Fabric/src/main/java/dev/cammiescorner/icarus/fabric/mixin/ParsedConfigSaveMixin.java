package dev.cammiescorner.icarus.fabric.mixin;

import com.teamresourceful.resourcefulconfig.common.loader.ParsedConfig;
import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.fabric.IcarusFabricGameRules;
import dev.cammiescorner.icarus.fabric.IcarusFabricServerHolder;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Resourceful Config persists via {@link ParsedConfig#save()}; hook the tail so server 
 * game rules match {@link dev.cammiescorner.icarus.IcarusConfig} immediately after a save
 */
@Mixin(ParsedConfig.class)
public abstract class ParsedConfigSaveMixin {

    @Inject(method = "save", at = @At("TAIL"))
    private void icarus$afterResourcefulConfigSave(CallbackInfo ci) {
        ParsedConfig self = (ParsedConfig) (Object) this;
        if (!Icarus.MODID.equals(self.id())) {
            return;
        }
        MinecraftServer server = IcarusFabricServerHolder.get();
        if (server == null) {
            return;
        }
        IcarusFabricGameRules.syncGameRulesFromConfigIfNeeded(server);
    }
}
