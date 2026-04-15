package dev.cammiescorner.icarus.client;

import dev.cammiescorner.icarus.api.IcarusPlayerValues;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public class ClientPlayerFallbackValues implements IcarusPlayerValues {

    private final float wingsSpeed;
    private final boolean armorSlows;
    private final float maxSlowedMultiplier;
    private final boolean canLoopDeLoop;
    private final float requiredFoodAmount;
    private final boolean flyingUsesHunger;
    private final boolean flyingReducesWingDurability;
    private final boolean canSlowFall;
    private final float exhaustionAmount;
    private final boolean maxHeightEnabled;
    private final int maxHeightAboveWorld;
    private final int wingsDurability;
    private final float flyingTargetRadius;

    public ClientPlayerFallbackValues(float wingsSpeed, float maxSlowedMultiplier, boolean armorSlows, boolean canLoopDeLoop,
                                      float requiredFoodAmount, boolean flyingUsesHunger, boolean flyingReducesWingDurability,
                                      boolean canSlowFall, float exhaustionAmount, boolean maxHeightEnabled,
                                      int maxHeightAboveWorld, int wingsDurability, float flyingTargetRadius) {
        this.wingsSpeed = wingsSpeed;
        this.maxSlowedMultiplier = maxSlowedMultiplier;
        this.armorSlows = armorSlows;
        this.canLoopDeLoop = canLoopDeLoop;
        this.requiredFoodAmount = requiredFoodAmount;
        this.flyingUsesHunger = flyingUsesHunger;
        this.flyingReducesWingDurability = flyingReducesWingDurability;
        this.canSlowFall = canSlowFall;
        this.exhaustionAmount = exhaustionAmount;
        this.maxHeightEnabled = maxHeightEnabled;
        this.maxHeightAboveWorld = maxHeightAboveWorld;
        this.wingsDurability = wingsDurability;
        this.flyingTargetRadius = flyingTargetRadius;
    }

    @Override
    public float wingsSpeed() {
        return wingsSpeed;
    }

    @Override
    public float maxSlowedMultiplier() {
        return maxSlowedMultiplier;
    }

    @Override
    public boolean armorSlows() {
        return armorSlows;
    }

    @Override
    public boolean canLoopDeLoop() {
        return canLoopDeLoop;
    }

    @Override
    public boolean canSlowFall() {
        return canSlowFall;
    }

    @Override
    public float exhaustionAmount() {
        return exhaustionAmount;
    }

    @Override
    public int maxHeightAboveWorld() {
        return maxHeightAboveWorld;
    }

    @Override
    public boolean maxHeightEnabled() {
        return maxHeightEnabled;
    }

    @Override
    public float requiredFoodAmount() {
        return requiredFoodAmount;
    }

    @Override
    public boolean flyingUsesHunger() {
        return flyingUsesHunger;
    }

    @Override
    public boolean flyingReducesWingDurability() {
        return flyingReducesWingDurability;
    }

    @Override
    public int wingsDurability() {
        return wingsDurability;
    }

    @Override
    public float flyingTargetRadius() {
        return flyingTargetRadius;
    }

    /**
     * used to test whether an entity is allowed to do a loop clientside,
     * with special handling for the current client player entity.
     */
    public static boolean canClientLoopDeLoop(LivingEntity entity) {
        return IcarusClientConfig.canLoopDeLoop || entity != Minecraft.getInstance().player;
    }
}
