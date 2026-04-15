package dev.cammiescorner.icarus.fabric.mixin;

import com.teamresourceful.resourcefulconfig.common.loader.ParsedConfig;
import java.io.File;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ParsedConfig.class)
public interface ParsedConfigInvoker {

    @Invoker("getConfigFile")
    File icarus$getConfigFile();
}
