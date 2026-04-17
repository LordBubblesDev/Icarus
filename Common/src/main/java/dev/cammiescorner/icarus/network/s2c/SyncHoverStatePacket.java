package dev.cammiescorner.icarus.network.s2c;

import commonnetwork.api.Dispatcher;
import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.api.HoveringEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Syncs hover standby, vertical anchor, and world-time phase anchor so every client uses the same bob phase
 * (entity tickCount alone is not aligned across machines for remote players).
 */
public record SyncHoverStatePacket(int entityId, boolean hovering, double centerY, long hoverBobAnchorGameTime) {
    public static final Identifier ID = Icarus.id("sync_hover_state");
    public static final CustomPacketPayload.Type<CustomPacketPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, SyncHoverStatePacket> STREAM_CODEC = StreamCodec.ofMember(SyncHoverStatePacket::encode, SyncHoverStatePacket::decode);

    private void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeBoolean(hovering);
        buf.writeDouble(centerY);
        buf.writeLong(hoverBobAnchorGameTime);
    }

    private static SyncHoverStatePacket decode(FriendlyByteBuf buf) {
        return new SyncHoverStatePacket(buf.readVarInt(), buf.readBoolean(), buf.readDouble(), buf.readLong());
    }

    public static void send(ServerPlayer to, int entityId, boolean hovering, double centerY, long hoverBobAnchorGameTime) {
        Dispatcher.sendToClient(new SyncHoverStatePacket(entityId, hovering, centerY, hoverBobAnchorGameTime), to);
    }

    public static void handle(PacketContext<SyncHoverStatePacket> ctx) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            var msg = ctx.message();
            var entity = Minecraft.getInstance().level.getEntity(msg.entityId());
            if (entity instanceof Player player && player instanceof HoveringEntity hover) {
                if (msg.hovering()) {
                    hover.icarus$setHoverCenterY(msg.centerY());
                    hover.icarus$setHoverBobAnchorGameTime(msg.hoverBobAnchorGameTime());
                    hover.icarus$setHoverStandby(true);
                } else {
                    hover.icarus$setHoverStandby(false);
                }
            }
        });
    }
}
