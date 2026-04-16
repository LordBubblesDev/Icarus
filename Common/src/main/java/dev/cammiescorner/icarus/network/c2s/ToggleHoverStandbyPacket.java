package dev.cammiescorner.icarus.network.c2s;

import commonnetwork.api.Dispatcher;
import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.api.HoveringEntity;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.util.Mth;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ToggleHoverStandbyPacket(boolean enabled) {
    public static final Identifier ID = Icarus.id("toggle_hover_standby");
    public static final CustomPacketPayload.Type<CustomPacketPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ToggleHoverStandbyPacket> STREAM_CODEC = StreamCodec.ofMember(ToggleHoverStandbyPacket::encode, ToggleHoverStandbyPacket::decode);

    private void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    private static ToggleHoverStandbyPacket decode(FriendlyByteBuf buf) {
        return new ToggleHoverStandbyPacket(buf.readBoolean());
    }

    public static void sendToServer(boolean enabled) {
        Dispatcher.sendToServer(new ToggleHoverStandbyPacket(enabled));
    }

    public static void handle(PacketContext<ToggleHoverStandbyPacket> ctx) {
        var player = ctx.sender();
        if (!(player instanceof HoveringEntity hovering) || !IcarusHelper.hasWings(player)) {
            return;
        }

        IcarusHelper.setHoverStandby(player, ctx.message().enabled());
        if (ctx.message().enabled()) {
            hovering.icarus$setHoverCenterY(player.getY());
            player.fallDistance = 0F;
            var move = player.getDeltaMovement();
            double clampedY = Mth.clamp(move.y, -0.08D, 0.08D);
            player.setDeltaMovement(move.x, clampedY, move.z);
            if (player.isFallFlying()) {
                player.stopFallFlying();
            }
        } else {
            player.setNoGravity(false);
        }
    }
}
