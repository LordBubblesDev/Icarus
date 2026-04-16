package dev.cammiescorner.icarus.client;

public interface IcarusSlowFallRenderState {
    boolean icarus$isSlowFallingWithWings();

    void icarus$setSlowFallingWithWings(boolean value);

    boolean icarus$isHoveringWithWings();

    void icarus$setHoveringWithWings(boolean value);

    float icarus$getHoverPhase();

    void icarus$setHoverPhase(float value);
}
