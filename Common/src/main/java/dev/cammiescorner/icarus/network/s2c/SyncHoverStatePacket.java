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

public record SyncHoverStatePacket(int entityId, boolean hovering) {
    public static final Identifier ID = Icarus.id("sync_hover_state");
    public static final CustomPacketPayload.Type<CustomPacketPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, SyncHoverStatePacket> STREAM_CODEC = StreamCodec.ofMember(SyncHoverStatePacket::encode, SyncHoverStatePacket::decode);

    private void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeBoolean(hovering);
    }

    private static SyncHoverStatePacket decode(FriendlyByteBuf buf) {
        return new SyncHoverStatePacket(buf.readVarInt(), buf.readBoolean());
    }

    public static void send(ServerPlayer player, int entityId, boolean hovering) {
        Dispatcher.sendToClient(new SyncHoverStatePacket(entityId, hovering), player);
    }

    public static void handle(PacketContext<SyncHoverStatePacket> ctx) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            var entity = Minecraft.getInstance().level.getEntity(ctx.message().entityId());
            if (entity instanceof Player player && player instanceof HoveringEntity hover) {
                hover.icarus$setHoverStandby(ctx.message().hovering());
            }
        });
    }
}
