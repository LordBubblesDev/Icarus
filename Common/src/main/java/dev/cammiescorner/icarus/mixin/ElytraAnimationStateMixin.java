package dev.cammiescorner.icarus.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.icarus.api.HoveringEntity;
import dev.cammiescorner.icarus.api.SlowFallingEntity;
import net.minecraft.world.entity.ElytraAnimationState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * {@link ElytraAnimationState#tick} picks crouch vs idle targets using {@link LivingEntity#isCrouching()}.
 * Icarus slow-fall forces a crouch-like pose on the render state for the body, but without this hook the
 * animation state still interpolates toward the idle elytra pose, so {@link net.minecraft.client.renderer.entity.state.HumanoidRenderState#elytraRotX}
 * does not match vanilla elytra while sneaking. Treat slow-fall with wings like crouch for rotation targets.
 */
@Mixin(ElytraAnimationState.class)
public abstract class ElytraAnimationStateMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isCrouching()Z"))
    private boolean icarus$useCrouchElytraTargetsWhenSlowFalling(LivingEntity entity, Operation<Boolean> original) {
        if (original.call(entity)) {
            return true;
        }
        return (entity instanceof SlowFallingEntity slow && slow.icarus$isSlowFalling())
            || (entity instanceof HoveringEntity hover && hover.icarus$isHoverStandby());
    }
}
