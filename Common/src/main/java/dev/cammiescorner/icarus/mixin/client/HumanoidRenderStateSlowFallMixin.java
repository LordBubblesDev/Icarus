package dev.cammiescorner.icarus.mixin.client;

import dev.cammiescorner.icarus.client.IcarusSlowFallRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public abstract class HumanoidRenderStateSlowFallMixin implements IcarusSlowFallRenderState {
    @Unique
    private boolean icarus$slowFallingWithWings;

    @Override
    public boolean icarus$isSlowFallingWithWings() {
        return icarus$slowFallingWithWings;
    }

    @Override
    public void icarus$setSlowFallingWithWings(boolean value) {
        icarus$slowFallingWithWings = value;
    }
}
