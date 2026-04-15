package dev.cammiescorner.icarus.mixin.client;

import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerSlowFallMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void icarus$tickSlowFallLast(CallbackInfo ci) {
        IcarusHelper.tickIcarusSlowFall((AbstractClientPlayer) (Object) this);
    }
}
