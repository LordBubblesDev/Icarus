package dev.cammiescorner.icarus.mixin.client;

import dev.cammiescorner.icarus.api.SlowFallingEntity;
import dev.cammiescorner.icarus.client.IcarusSlowFallRenderState;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
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
            && slow.icarus$isSlowFalling()
            && IcarusHelper.hasWings(entity);
        ((IcarusSlowFallRenderState) state).icarus$setSlowFallingWithWings(slowFallingWithWings);
        if (slowFallingWithWings) {
            state.isCrouching = true;
        }
    }
}
