package dev.cammiescorner.icarus.fabric;

import dev.cammiescorner.icarus.IcarusConfig;
import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.network.s2c.SyncConfigValuesPacket;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class IcarusFabricGameRules {
    private static final int DEFAULT_FLIGHT_SPEED = Math.round(IcarusConfig.wingsSpeed * 10000.0F);

    public static final GameRule<Boolean> ARMOR_REDUCE_FLIGHT_SPEED = GameRuleBuilder.forBoolean(IcarusConfig.armorSlows)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("armor_reduce_flight_speed"));

    public static final GameRule<Integer> WINGS_FLIGHT_SPEED = GameRuleBuilder.forInteger(DEFAULT_FLIGHT_SPEED)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("wings_flight_speed"));
    public static final GameRule<Boolean> FLYING_REDUCES_WING_DURABILITY = GameRuleBuilder.forBoolean(true)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("flying_reduces_wing_durability"));
    public static final GameRule<Boolean> FLYING_USES_HUNGER = GameRuleBuilder.forBoolean(true)
        .category(GameRuleCategory.PLAYER)
        .buildAndRegister(Icarus.id("flying_uses_hunger"));

    private IcarusFabricGameRules() {
    }

    public static void init() {
        // Register sync hooks so client config mirrors server gamerules.
        GameRuleEvents.changeCallback(ARMOR_REDUCE_FLIGHT_SPEED).register((value, server) ->
            server.getPlayerList().getPlayers().forEach(SyncConfigValuesPacket::send)
        );
        GameRuleEvents.changeCallback(WINGS_FLIGHT_SPEED).register((value, server) ->
            server.getPlayerList().getPlayers().forEach(SyncConfigValuesPacket::send)
        );
        GameRuleEvents.changeCallback(FLYING_REDUCES_WING_DURABILITY).register((value, server) ->
            server.getPlayerList().getPlayers().forEach(SyncConfigValuesPacket::send)
        );
        GameRuleEvents.changeCallback(FLYING_USES_HUNGER).register((value, server) ->
            server.getPlayerList().getPlayers().forEach(SyncConfigValuesPacket::send)
        );
    }

    public static boolean armorSlows(ServerLevel level) {
        return level.getGameRules().get(ARMOR_REDUCE_FLIGHT_SPEED);
    }

    public static float wingsSpeed(ServerLevel level) {
        return Math.max(0, level.getGameRules().get(WINGS_FLIGHT_SPEED)) / 10000.0F;
    }

    public static boolean flyingReducesWingDurability(ServerLevel level) {
        return level.getGameRules().get(FLYING_REDUCES_WING_DURABILITY);
    }

    public static boolean flyingUsesHunger(ServerLevel level) {
        return level.getGameRules().get(FLYING_USES_HUNGER);
    }
}
