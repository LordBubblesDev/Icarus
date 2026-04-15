package dev.cammiescorner.icarus.fabric;

import dev.cammiescorner.icarus.IcarusConfig;
import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.network.s2c.SyncConfigValuesPacket;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class IcarusFabricGameRules {
    private static final double DEFAULT_FLIGHT_SPEED = IcarusConfig.wingsSpeed;
    private static boolean pushingConfigToGameRules;

    public static final GameRule<Boolean> ARMOR_SLOWS = GameRuleBuilder.forBoolean(IcarusConfig.armorSlows)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("armor_slows"));
    public static final GameRule<Double> WINGS_SPEED = GameRuleBuilder.forDouble(DEFAULT_FLIGHT_SPEED)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("wings_speed"));
    public static final GameRule<Boolean> FLYING_REDUCES_WING_DURABILITY = GameRuleBuilder.forBoolean(IcarusConfig.flyingReducesWingDurability)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("flying_reduces_wing_durability"));
    public static final GameRule<Boolean> FLYING_USES_HUNGER = GameRuleBuilder.forBoolean(IcarusConfig.flyingUsesHunger)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("flying_uses_hunger"));
    public static final GameRule<Boolean> CAN_LOOP_DE_LOOP = GameRuleBuilder.forBoolean(IcarusConfig.canLoopDeLoop)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("can_loop_de_loop"));
    public static final GameRule<Boolean> CAN_SLOW_FALL = GameRuleBuilder.forBoolean(IcarusConfig.canSlowFall)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("can_slow_fall"));
    public static final GameRule<Double> MAX_SLOWED_MULTIPLIER = GameRuleBuilder.forDouble(IcarusConfig.maxSlowedMultiplier)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("max_slowed_multiplier"));
    public static final GameRule<Integer> WINGS_DURABILITY = GameRuleBuilder.forInteger(IcarusConfig.wingsDurability)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("wings_durability"));
    public static final GameRule<Double> EXHAUSTION_AMOUNT = GameRuleBuilder.forDouble(IcarusConfig.exhaustionAmount)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("exhaustion_amount"));
    public static final GameRule<Double> REQUIRED_FOOD_AMOUNT = GameRuleBuilder.forDouble(IcarusConfig.requiredFoodAmount)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("required_food_amount"));
    public static final GameRule<Boolean> MAX_HEIGHT_ENABLED = GameRuleBuilder.forBoolean(IcarusConfig.maxHeightEnabled)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("max_height_enabled"));
    public static final GameRule<Integer> MAX_HEIGHT_ABOVE_WORLD = GameRuleBuilder.forInteger(IcarusConfig.maxHeightAboveWorld)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("max_height_above_world"));
    public static final GameRule<Double> FLYING_TARGET_RADIUS = GameRuleBuilder.forDouble(IcarusConfig.flyingTargetRadius)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("flying_target_radius"));

    private IcarusFabricGameRules() {
    }

    private static boolean speedsDiffer(double a, double b) {
        return Math.abs(a - b) > 1e-7;
    }

    /**
     * Copies values from the world's game rules into {@link IcarusConfig} and saves the config file when they differ.
     * Used when a game rule changes (e.g. {@code /gamerule}) and on server start so the file matches the world.
     */
    public static void syncConfigFileFromGameRules(ServerLevel level) {
        var rules = level.getGameRules();
        boolean changed = false;

        double grSpeed = Math.max(0.0, rules.get(WINGS_SPEED));
        if (speedsDiffer(grSpeed, IcarusConfig.wingsSpeed)) {
            IcarusConfig.wingsSpeed = (float) grSpeed;
            changed = true;
        }

        boolean grArmor = rules.get(ARMOR_SLOWS);
        if (IcarusConfig.armorSlows != grArmor) {
            IcarusConfig.armorSlows = grArmor;
            changed = true;
        }

        boolean grDurability = rules.get(FLYING_REDUCES_WING_DURABILITY);
        if (IcarusConfig.flyingReducesWingDurability != grDurability) {
            IcarusConfig.flyingReducesWingDurability = grDurability;
            changed = true;
        }

        boolean grHunger = rules.get(FLYING_USES_HUNGER);
        if (IcarusConfig.flyingUsesHunger != grHunger) {
            IcarusConfig.flyingUsesHunger = grHunger;
            changed = true;
        }

        boolean grLoop = rules.get(CAN_LOOP_DE_LOOP);
        if (IcarusConfig.canLoopDeLoop != grLoop) {
            IcarusConfig.canLoopDeLoop = grLoop;
            changed = true;
        }

        boolean grSlowFall = rules.get(CAN_SLOW_FALL);
        if (IcarusConfig.canSlowFall != grSlowFall) {
            IcarusConfig.canSlowFall = grSlowFall;
            changed = true;
        }

        double grMaxSlowed = rules.get(MAX_SLOWED_MULTIPLIER);
        if (speedsDiffer(grMaxSlowed, IcarusConfig.maxSlowedMultiplier)) {
            IcarusConfig.maxSlowedMultiplier = (float) grMaxSlowed;
            changed = true;
        }

        int grWingsDur = rules.get(WINGS_DURABILITY);
        if (IcarusConfig.wingsDurability != grWingsDur) {
            IcarusConfig.wingsDurability = grWingsDur;
            changed = true;
        }

        double grExhaust = rules.get(EXHAUSTION_AMOUNT);
        if (speedsDiffer(grExhaust, IcarusConfig.exhaustionAmount)) {
            IcarusConfig.exhaustionAmount = (float) grExhaust;
            changed = true;
        }

        double grFood = rules.get(REQUIRED_FOOD_AMOUNT);
        if (speedsDiffer(grFood, IcarusConfig.requiredFoodAmount)) {
            IcarusConfig.requiredFoodAmount = (float) grFood;
            changed = true;
        }

        boolean grMaxH = rules.get(MAX_HEIGHT_ENABLED);
        if (IcarusConfig.maxHeightEnabled != grMaxH) {
            IcarusConfig.maxHeightEnabled = grMaxH;
            changed = true;
        }

        int grAbove = rules.get(MAX_HEIGHT_ABOVE_WORLD);
        if (IcarusConfig.maxHeightAboveWorld != grAbove) {
            IcarusConfig.maxHeightAboveWorld = grAbove;
            changed = true;
        }

        double grRadius = rules.get(FLYING_TARGET_RADIUS);
        if (speedsDiffer(grRadius, IcarusConfig.flyingTargetRadius)) {
            IcarusConfig.flyingTargetRadius = (float) grRadius;
            changed = true;
        }

        if (changed) {
            Icarus.saveIcarusConfigToDisk();
        }
    }

    /**
     * Pushes {@link IcarusConfig} into game rules when Resourceful Config (or a manual edit) changed the static fields
     * while the server is running.
     */
    public static void syncGameRulesFromConfigIfNeeded(MinecraftServer server) {
        pushingConfigToGameRules = true;
        try {
            ServerLevel level = server.overworld();
            var rules = level.getGameRules();

            double desiredSpeed = Math.max(0.0, IcarusConfig.wingsSpeed);
            double currentSpeed = Math.max(0.0, rules.get(WINGS_SPEED));
            if (speedsDiffer(desiredSpeed, currentSpeed)) {
                rules.set(WINGS_SPEED, desiredSpeed, server);
            }

            boolean desiredArmor = IcarusConfig.armorSlows;
            boolean currentArmor = rules.get(ARMOR_SLOWS);
            if (desiredArmor != currentArmor) {
                rules.set(ARMOR_SLOWS, desiredArmor, server);
            }

            boolean desiredDurability = IcarusConfig.flyingReducesWingDurability;
            boolean currentDurability = rules.get(FLYING_REDUCES_WING_DURABILITY);
            if (desiredDurability != currentDurability) {
                rules.set(FLYING_REDUCES_WING_DURABILITY, desiredDurability, server);
            }

            boolean desiredHunger = IcarusConfig.flyingUsesHunger;
            boolean currentHunger = rules.get(FLYING_USES_HUNGER);
            if (desiredHunger != currentHunger) {
                rules.set(FLYING_USES_HUNGER, desiredHunger, server);
            }

            boolean desiredLoop = IcarusConfig.canLoopDeLoop;
            boolean currentLoop = rules.get(CAN_LOOP_DE_LOOP);
            if (desiredLoop != currentLoop) {
                rules.set(CAN_LOOP_DE_LOOP, desiredLoop, server);
            }

            boolean desiredSlowFall = IcarusConfig.canSlowFall;
            boolean currentSlowFall = rules.get(CAN_SLOW_FALL);
            if (desiredSlowFall != currentSlowFall) {
                rules.set(CAN_SLOW_FALL, desiredSlowFall, server);
            }

            double desiredMaxSlowed = IcarusConfig.maxSlowedMultiplier;
            double currentMaxSlowed = rules.get(MAX_SLOWED_MULTIPLIER);
            if (speedsDiffer(desiredMaxSlowed, currentMaxSlowed)) {
                rules.set(MAX_SLOWED_MULTIPLIER, desiredMaxSlowed, server);
            }

            int desiredWingsDur = IcarusConfig.wingsDurability;
            int currentWingsDur = rules.get(WINGS_DURABILITY);
            if (desiredWingsDur != currentWingsDur) {
                rules.set(WINGS_DURABILITY, desiredWingsDur, server);
            }

            double desiredExhaust = IcarusConfig.exhaustionAmount;
            double currentExhaust = rules.get(EXHAUSTION_AMOUNT);
            if (speedsDiffer(desiredExhaust, currentExhaust)) {
                rules.set(EXHAUSTION_AMOUNT, desiredExhaust, server);
            }

            double desiredFood = IcarusConfig.requiredFoodAmount;
            double currentFood = rules.get(REQUIRED_FOOD_AMOUNT);
            if (speedsDiffer(desiredFood, currentFood)) {
                rules.set(REQUIRED_FOOD_AMOUNT, desiredFood, server);
            }

            boolean desiredMaxHeight = IcarusConfig.maxHeightEnabled;
            boolean currentMaxHeight = rules.get(MAX_HEIGHT_ENABLED);
            if (desiredMaxHeight != currentMaxHeight) {
                rules.set(MAX_HEIGHT_ENABLED, desiredMaxHeight, server);
            }

            int desiredAbove = IcarusConfig.maxHeightAboveWorld;
            int currentAbove = rules.get(MAX_HEIGHT_ABOVE_WORLD);
            if (desiredAbove != currentAbove) {
                rules.set(MAX_HEIGHT_ABOVE_WORLD, desiredAbove, server);
            }

            double desiredRadius = IcarusConfig.flyingTargetRadius;
            double currentRadius = rules.get(FLYING_TARGET_RADIUS);
            if (speedsDiffer(desiredRadius, currentRadius)) {
                rules.set(FLYING_TARGET_RADIUS, desiredRadius, server);
            }
        } finally {
            pushingConfigToGameRules = false;
        }
        server.getPlayerList().getPlayers().forEach(SyncConfigValuesPacket::send);
    }

    private static void onGameRuleChanged(MinecraftServer server) {
        if (pushingConfigToGameRules) {
            return;
        }
        syncConfigFileFromGameRules(server.overworld());
        server.getPlayerList().getPlayers().forEach(SyncConfigValuesPacket::send);
    }

    public static void init() {
        GameRuleEvents.changeCallback(ARMOR_SLOWS).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(WINGS_SPEED).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(FLYING_REDUCES_WING_DURABILITY).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(FLYING_USES_HUNGER).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(CAN_LOOP_DE_LOOP).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(CAN_SLOW_FALL).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(MAX_SLOWED_MULTIPLIER).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(WINGS_DURABILITY).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(EXHAUSTION_AMOUNT).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(REQUIRED_FOOD_AMOUNT).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(MAX_HEIGHT_ENABLED).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(MAX_HEIGHT_ABOVE_WORLD).register((value, server) -> onGameRuleChanged(server));
        GameRuleEvents.changeCallback(FLYING_TARGET_RADIUS).register((value, server) -> onGameRuleChanged(server));
    }

    public static boolean armorSlows(ServerLevel level) {
        return level.getGameRules().get(ARMOR_SLOWS);
    }

    public static float wingsSpeed(ServerLevel level) {
        return (float) Math.max(0.0, level.getGameRules().get(WINGS_SPEED));
    }

    public static boolean flyingReducesWingDurability(ServerLevel level) {
        return level.getGameRules().get(FLYING_REDUCES_WING_DURABILITY);
    }

    public static boolean flyingUsesHunger(ServerLevel level) {
        return level.getGameRules().get(FLYING_USES_HUNGER);
    }

    public static boolean canLoopDeLoop(ServerLevel level) {
        return level.getGameRules().get(CAN_LOOP_DE_LOOP);
    }

    public static boolean canSlowFall(ServerLevel level) {
        return level.getGameRules().get(CAN_SLOW_FALL);
    }

    public static float maxSlowedMultiplier(ServerLevel level) {
        return level.getGameRules().get(MAX_SLOWED_MULTIPLIER).floatValue();
    }

    public static int wingsDurability(ServerLevel level) {
        return level.getGameRules().get(WINGS_DURABILITY);
    }

    public static float exhaustionAmount(ServerLevel level) {
        return level.getGameRules().get(EXHAUSTION_AMOUNT).floatValue();
    }

    public static float requiredFoodAmount(ServerLevel level) {
        return level.getGameRules().get(REQUIRED_FOOD_AMOUNT).floatValue();
    }

    public static boolean maxHeightEnabled(ServerLevel level) {
        return level.getGameRules().get(MAX_HEIGHT_ENABLED);
    }

    public static int maxHeightAboveWorld(ServerLevel level) {
        return level.getGameRules().get(MAX_HEIGHT_ABOVE_WORLD);
    }

    public static float flyingTargetRadius(ServerLevel level) {
        return level.getGameRules().get(FLYING_TARGET_RADIUS).floatValue();
    }
}
