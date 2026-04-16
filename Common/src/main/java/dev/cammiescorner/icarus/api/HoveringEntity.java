package dev.cammiescorner.icarus.api;

public interface HoveringEntity {
    boolean icarus$isHoverStandby();

    void icarus$setHoverStandby(boolean value);

    double icarus$getHoverCenterY();

    void icarus$setHoverCenterY(double value);

    float icarus$getHoverPhase();

    void icarus$setHoverPhase(float value);
}
