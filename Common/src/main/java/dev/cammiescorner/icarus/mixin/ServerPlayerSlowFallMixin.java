package dev.cammiescorner.icarus.mixin;

import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerSlowFallMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void icarus$tickSlowFallLast(CallbackInfo ci) {
        IcarusHelper.tickIcarusSlowFall((ServerPlayer) (Object) this);
    }
}
