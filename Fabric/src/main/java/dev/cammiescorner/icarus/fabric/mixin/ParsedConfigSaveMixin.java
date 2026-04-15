package dev.cammiescorner.icarus.fabric.mixin;

import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import com.teamresourceful.resourcefulconfig.common.loader.ParsedConfig;
import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.fabric.IcarusFabricGameRules;
import dev.cammiescorner.icarus.fabric.IcarusFabricServerHolder;
import dev.cammiescorner.icarus.util.IcarusConfigDecimalNormalization;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Resourceful Config persists via {@link ParsedConfig#save()}; hook the tail so server 
 * game rules match {@link dev.cammiescorner.icarus.IcarusConfig} immediately after a save
 */
@Mixin(ParsedConfig.class)
public abstract class ParsedConfigSaveMixin {

    private boolean icarus$isIcarusConfig(ParsedConfig cfg) {
        if (cfg.id() != null && cfg.id().startsWith(Icarus.MODID)) {
            return true;
        }

        // Some Resourceful Config ids are not exactly the mod id; file name is the stable fallback.
        String fileName = ((ParsedConfigInvoker) (Object) this).icarus$getConfigFile().getName();
        return fileName.startsWith(Icarus.MODID);
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void icarus$normalizeDecimalsBeforeLoad(Consumer<ConfigPatchEvent> patchHandler, CallbackInfo ci) {
        ParsedConfig self = (ParsedConfig) (Object) this;
        if (!icarus$isIcarusConfig(self)) {
            return;
        }
        IcarusConfigDecimalNormalization.normalizeFileIfPresent(((ParsedConfigInvoker) (Object) this).icarus$getConfigFile().toPath());
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void icarus$afterResourcefulConfigSave(CallbackInfo ci) {
        ParsedConfig self = (ParsedConfig) (Object) this;
        if (!icarus$isIcarusConfig(self)) {
            return;
        }
        IcarusConfigDecimalNormalization.normalizeFileIfPresent(((ParsedConfigInvoker) (Object) this).icarus$getConfigFile().toPath());
        MinecraftServer server = IcarusFabricServerHolder.get();
        if (server == null) {
            return;
        }
        IcarusFabricGameRules.syncGameRulesFromConfigIfNeeded(server);
    }
}
