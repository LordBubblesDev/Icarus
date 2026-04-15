package dev.cammiescorner.icarus.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
public abstract class PlayerMixin extends LivingEntity implements SlowFallingEntity {

    @Unique
    private boolean icarus$slowFalling;

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

    @ModifyReturnValue(method = "getDesiredPose", at = @At("RETURN"))
    private Pose icarus$forceCrouchPoseWhileSlowFalling(Pose original) {
        if (original == Pose.STANDING && icarus$slowFalling && IcarusHelper.hasWings((Player) (Object) this)) {
            return Pose.CROUCHING;
        }
        return original;
    }
}
