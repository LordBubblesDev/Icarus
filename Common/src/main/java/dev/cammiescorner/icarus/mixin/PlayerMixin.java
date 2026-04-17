package dev.cammiescorner.icarus.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.icarus.api.HoveringEntity;
import dev.cammiescorner.icarus.api.SlowFallingEntity;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements SlowFallingEntity, HoveringEntity {

    @Unique
    private boolean icarus$slowFalling;
    @Unique
    private boolean icarus$hoverStandby;
    @Unique
    private double icarus$hoverCenterY;
    @Unique
    private long icarus$hoverBobAnchorGameTime = -1L;
    @Unique
    private float icarus$hoverPhase;

    private PlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
        throw new UnsupportedOperationException();
    }

    @Override
    public void icarus$setSlowFalling(boolean slowFalling) {
        this.icarus$slowFalling = slowFalling;
    }

    @Override
    public boolean icarus$isSlowFalling() {
        return icarus$slowFalling;
    }

    @Override
    public boolean icarus$isHoverStandby() {
        return icarus$hoverStandby;
    }

    @Override
    public void icarus$setHoverStandby(boolean value) {
        icarus$hoverStandby = value;
        if (!value) {
            icarus$hoverBobAnchorGameTime = -1L;
        }
    }

    @Override
    public double icarus$getHoverCenterY() {
        return icarus$hoverCenterY;
    }

    @Override
    public void icarus$setHoverCenterY(double value) {
        icarus$hoverCenterY = value;
    }

    @Override
    public long icarus$getHoverBobAnchorGameTime() {
        return icarus$hoverBobAnchorGameTime;
    }

    @Override
    public void icarus$setHoverBobAnchorGameTime(long gameTime) {
        this.icarus$hoverBobAnchorGameTime = gameTime;
    }

    @Override
    public float icarus$getHoverPhase() {
        return icarus$hoverPhase;
    }

    @Override
    public void icarus$setHoverPhase(float value) {
        icarus$hoverPhase = value;
    }

    @ModifyReturnValue(method = "getDesiredPose", at = @At("RETURN"))
    private Pose icarus$forceCrouchPoseWhileSlowFalling(Pose original) {
        if (original == Pose.STANDING && (icarus$slowFalling || icarus$hoverStandby) && IcarusHelper.hasWings((Player) (Object) this)) {
            return Pose.CROUCHING;
        }
        return original;
    }
}
