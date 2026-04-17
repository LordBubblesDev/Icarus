package dev.cammiescorner.icarus.mixin.client;

import dev.cammiescorner.icarus.api.HoveringEntity;
import dev.cammiescorner.icarus.api.SlowFallingEntity;
import dev.cammiescorner.icarus.client.IcarusSlowFallRenderState;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererSlowFallMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void icarus$markSlowFallWingAnimation(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        boolean slowFallingWithWings = entity instanceof AbstractClientPlayer
            && entity instanceof SlowFallingEntity slow
            && slow.icarus$isSlowFalling();
        boolean hoveringWithWings = entity instanceof AbstractClientPlayer
            && entity instanceof HoveringEntity hover
            && hover.icarus$isHoverStandby();

        IcarusSlowFallRenderState icarusState = (IcarusSlowFallRenderState) state;
        icarusState.icarus$setSlowFallingWithWings(slowFallingWithWings);
        icarusState.icarus$setHoveringWithWings(hoveringWithWings);
        if (entity instanceof HoveringEntity hover) {
            if (hover.icarus$isHoverStandby() && entity instanceof LivingEntity le) {
                icarusState.icarus$setHoverPhase(IcarusHelper.hoverBobPhase(le, hover));
            } else {
                icarusState.icarus$setHoverPhase(hover.icarus$getHoverPhase());
            }
        } else {
            icarusState.icarus$setHoverPhase(0.0F);
        }
        if (slowFallingWithWings || hoveringWithWings) {
            state.isCrouching = true;
        }
    }
}
