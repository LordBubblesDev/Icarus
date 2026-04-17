package dev.cammiescorner.icarus.api;

public interface HoveringEntity {
    boolean icarus$isHoverStandby();

    void icarus$setHoverStandby(boolean value);

    double icarus$getHoverCenterY();

    void icarus$setHoverCenterY(double value);

    /** {@link net.minecraft.world.level.Level#getGameTime()} when hover started; used so all clients share bob phase. */
    long icarus$getHoverBobAnchorGameTime();

    void icarus$setHoverBobAnchorGameTime(long gameTime);

    float icarus$getHoverPhase();

    void icarus$setHoverPhase(float value);
}
