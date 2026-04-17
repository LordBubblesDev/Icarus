package dev.cammiescorner.icarus.util;

import dev.cammiescorner.icarus.api.IcarusPlayerValues;
import dev.cammiescorner.icarus.api.HoveringEntity;
import dev.cammiescorner.icarus.api.SlowFallingEntity;
import dev.cammiescorner.icarus.client.IcarusClient;
import dev.cammiescorner.icarus.init.IcarusDimensionTypeTags;
import dev.cammiescorner.icarus.init.IcarusItemTags;
import dev.cammiescorner.icarus.init.IcarusStatusEffects;
import dev.cammiescorner.icarus.item.WingItem;
import dev.cammiescorner.icarus.network.c2s.ApplyBoostPacket;
import dev.cammiescorner.icarus.network.s2c.SyncConfigValuesPacket;
import dev.cammiescorner.icarus.network.s2c.SyncHoverStatePacket;
import dev.cammiescorner.icarus.IcarusConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class IcarusHelper {

    /** Vertical bob for hover (blocks). Uses {@link LivingEntity#tickCount} for phase so server and clients match. */
    public static final double HOVER_BOB_AMPLITUDE_BLOCKS = 0.86D;
    public static final float HOVER_BOB_PHASE_PER_TICK = 0.2F;

    @ApiStatus.Internal
    public static Predicate<LivingEntity> hasWings = entity -> false;

    @ApiStatus.Internal
    public static Function<LivingEntity, ItemStack> getEquippedWings = entity -> ItemStack.EMPTY;
    @ApiStatus.Internal
    public static BiPredicate<LivingEntity, ItemStack> equipFunc = (entity, stack) -> false;

    @ApiStatus.Internal
    public static IcarusPlayerValues fallbackValues = new ServerPlayerFallbackValues();
    @ApiStatus.Internal
    public static Function<LivingEntity, IcarusPlayerValues> configValuesProvider = entity -> fallbackValues;

    public static boolean onFallFlyingTick(LivingEntity entity, @Nullable ItemStack wings, boolean tick) {
        IcarusPlayerValues cfg = IcarusHelper.getConfigValues(entity);
        var level = entity.level();

        if(!level.isClientSide()) {
            // level stem -> the actual level instance
            // dimension type -> all levels of a given type
            // some level stems (namely, AE2's spatial storage dimension) may not be registered properly
            // first check the dimension type, then check the level itself; if either one is in the no fly tag, cancel flying and send a message
            if(level.dimensionTypeRegistration().is(IcarusDimensionTypeTags.NO_FLYING_ALLOWED) || level.registryAccess().lookupOrThrow(Registries.LEVEL_STEM).get(Registries.levelToLevelStem(level.dimension())).map(stemHolder -> stemHolder.is(cfg.noFlyingAllowedInLevels())).orElse(false)) {
                if (entity instanceof ServerPlayer player) {
                    stopFlying(player);
                    player.sendSystemMessage(Component.translatable("message.icarus.status.no_fly.dimension").withStyle(ChatFormatting.RED), true);
                }
                return false;
            }
        }

        if (entity.hasEffect(IcarusStatusEffects.flightlessHolder())) {
            if (entity instanceof Player player) {
                stopFlying(player);
                Component message = Component.translatable("message.icarus.status.no_fly.status_effect").withStyle(ChatFormatting.BLUE);
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendSystemMessage(message, true);
                } else {
                    IcarusClient.sendActionbarMessage(player, message);
                }
            }
            return false;
        }


        if (wings != null && (!(wings.getItem() instanceof WingItem wingItem) || !wingItem.isUsable(entity, wings))) {
            if (entity instanceof Player player) {
                stopFlying(player);
            }
            return false;
        }

        if (tick) {
            if ((cfg.canSlowFall() && entity.isShiftKeyDown()) || entity.isUnderWater()) {
                if (entity instanceof Player player) {
                    stopFlying(player);
                }
                return false;
            }

            if (cfg.flyingUsesHunger() && (wings == null || !wings.is(IcarusItemTags.FREE_FLIGHT)) && entity instanceof Player player && !player.isCreative()) {
                if(player.getFoodData().getFoodLevel() >= cfg.requiredFoodAmount() && IcarusClient.hasForwardFlightInput(player) && level.isClientSide()) {
                    ApplyBoostPacket.sendToServer();
                }

                if (player.getFoodData().getFoodLevel() < cfg.requiredFoodAmount()) {
                    stopFlying(player);
                    Component message = Component.translatable("message.icarus.status.no_fly.hunger").withStyle(ChatFormatting.BLUE);
                    if (entity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.sendSystemMessage(message, true);
                    } else {
                        IcarusClient.sendActionbarMessage(player, message);
                    }
                    return false;
                }
            }

            if (wings != null && wings.getItem() instanceof WingItem wingItem && !wingItem.onFlightTick(entity, wings, entity.getFallFlyingTicks() + 1)) {
                if (entity instanceof Player player) {
                    stopFlying(player);
                }
                return false;
            }
        }

        return true;
    }

    public static boolean hasWings(LivingEntity entity) {
        return hasWings.test(entity);
    }

    @Nullable
    public static ItemStack getEquippedWings(LivingEntity entity) {
        return getEquippedWings.apply(entity);
    }

    public static IcarusPlayerValues getConfigValues(LivingEntity entity) {
        return configValuesProvider.apply(entity);
    }

    /**
     * Bob phase in radians-ish units passed to {@link Math#sin(double)}. Uses world {@link net.minecraft.world.level.Level#getGameTime()}
     * minus a synced anchor so all clients match the server (entity {@code tickCount} does not).
     */
    public static float hoverBobPhase(LivingEntity entity, HoveringEntity hover) {
        long anchor = hover.icarus$getHoverBobAnchorGameTime();
        if (anchor < 0L) {
            return entity.tickCount * HOVER_BOB_PHASE_PER_TICK;
        }
        long delta = entity.level().getGameTime() - anchor;
        return delta * HOVER_BOB_PHASE_PER_TICK;
    }

    /** World-space Y for hover bob; same formula on server and local client ({@link #applyHoverPhysics}). */
    public static double hoverBobTargetY(LivingEntity entity, HoveringEntity hover) {
        double phase = hoverBobPhase(entity, hover);
        return hover.icarus$getHoverCenterY() + Math.sin(phase) * HOVER_BOB_AMPLITUDE_BLOCKS;
    }

    /** Server or local client: lock hover position and kill vertical inertia. */
    public static void applyHoverPhysics(Player player) {
        HoveringEntity hover = (HoveringEntity) player;
        player.setNoGravity(true);
        var move = player.getDeltaMovement();
        double ty = hoverBobTargetY(player, hover);
        player.setPos(player.getX(), ty, player.getZ());
        player.setDeltaMovement(move.x() * 0.1D, 0.0D, move.z() * 0.1D);
    }

    /** Called from {@code tick} TAIL mixins on server/client player (after movement). */
    public static void tickIcarusSlowFall(Player player) {
        SlowFallingEntity slow = (SlowFallingEntity) player;
        HoveringEntity hover = (HoveringEntity) player;
        boolean slowFalling = slow.icarus$isSlowFalling();
        boolean hovering = hover.icarus$isHoverStandby();
        if (!slowFalling && !hovering) {
            return;
        }

        if (player.isCreative() && player.getAbilities().flying) {
            player.setNoGravity(false);
            slow.icarus$setSlowFalling(false);
            setHoverStandby(player, false);
            return;
        }

        player.fallDistance = 0F;
        // Hover wing phase comes from tickCount in render; only advance stored phase for slow-fall (client).
        if (player.level().isClientSide() && slowFalling && !hovering) {
            hover.icarus$setHoverPhase(hover.icarus$getHoverPhase() + HOVER_BOB_PHASE_PER_TICK);
        }

        if (hovering && player.isShiftKeyDown()) {
            player.setNoGravity(false);
            setHoverStandby(player, false);
            slow.icarus$setSlowFalling(true);
            return;
        }

        if (player.onGround() || player.isInWater()) {
            player.setNoGravity(false);
            slow.icarus$setSlowFalling(false);
            setHoverStandby(player, false);
        } else {
            var move = player.getDeltaMovement();
            if (hovering) {
                // One authority for bob: tickCount-based sine on the server; local client mirrors in
                // AbstractClientPlayerSlowFallMixin so momentum/gravity prediction cannot drift.
                if (!player.level().isClientSide()) {
                    applyHoverPhysics(player);
                }
            } else if (slowFalling) {
                player.setNoGravity(false);
                float d = Mth.clamp(IcarusConfig.slowFallDescentPerTick, 0.02F, 2.0F);
                player.setDeltaMovement(move.x(), -d, move.z());
            }
        }
    }

    public static void stopFlying(Player player) {
        ((SlowFallingEntity) player).icarus$setSlowFalling(true);
        player.fallDistance = 0F;
        var move = player.getDeltaMovement();
        float d = Mth.clamp(IcarusConfig.slowFallDescentPerTick, 0.02F, 2.0F);
        player.setDeltaMovement(move.x(), Math.max(move.y(), -d), move.z());
        applyUpsideDownLoopCameraStabilization(player);
        player.stopFallFlying();
    }

    private static void applyUpsideDownLoopCameraStabilization(Player player) {
        if (player.getXRot() < -90 || player.getXRot() > 90) {
            float offset = (player.getXRot() < -90 ? player.getXRot() + 180 : player.getXRot() - 180) * 2;
            player.setXRot((player.getXRot() < -90 ? 180 + offset : -180 - offset) + player.getXRot());
            player.setYRot(180 + player.getYRot());
        }
    }

    public static void onServerPlayerJoin(ServerPlayer player) {
        SyncConfigValuesPacket.send(player);
        if (player.level().getServer() != null) {
            for (ServerPlayer other : player.level().getServer().getPlayerList().getPlayers()) {
                HoveringEntity h = (HoveringEntity) other;
                boolean on = h.icarus$isHoverStandby();
                SyncHoverStatePacket.send(player, other.getId(), on, on ? h.icarus$getHoverCenterY() : 0.0D, on ? h.icarus$getHoverBobAnchorGameTime() : -1L);
            }
        }
    }

    public static void setHoverStandby(Player player, boolean enabled) {
        HoveringEntity hover = (HoveringEntity) player;
        if (hover.icarus$isHoverStandby() == enabled) {
            return;
        }
        hover.icarus$setHoverStandby(enabled);
        if (player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level().getServer() != null) {
                double cy = enabled ? hover.icarus$getHoverCenterY() : 0.0D;
                long anchor = enabled ? hover.icarus$getHoverBobAnchorGameTime() : -1L;
                for (ServerPlayer target : serverPlayer.level().getServer().getPlayerList().getPlayers()) {
                    SyncHoverStatePacket.send(target, player.getId(), enabled, cy, anchor);
                }
            }
        }
    }
}
