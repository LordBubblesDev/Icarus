package dev.cammiescorner.icarus.mixin.client;

import dev.cammiescorner.icarus.api.HoveringEntity;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerSlowFallMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void icarus$tickSlowFallLast(CallbackInfo ci) {
        AbstractClientPlayer self = (AbstractClientPlayer) (Object) this;
        IcarusHelper.tickIcarusSlowFall(self);
        // Match server hover Y this tick so client prediction/gravity does not leave the body behind the wings.
        if (self instanceof HoveringEntity hover && hover.icarus$isHoverStandby() && Minecraft.getInstance().player == self) {
            IcarusHelper.applyHoverPhysics(self);
        }
    }
}
