package net.electro.elementalist.networking;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.networking.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Elementalist.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(UnlockSpellC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UnlockSpellC2SPacket::new)
                .encoder(UnlockSpellC2SPacket::toBytes)
                .consumerMainThread(UnlockSpellC2SPacket::handle)
                .add();

        net.messageBuilder(SelectSpellC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SelectSpellC2SPacket::new)
                .encoder(SelectSpellC2SPacket::toBytes)
                .consumerMainThread(SelectSpellC2SPacket::handle)
                .add();

        net.messageBuilder(ActivateSpellC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ActivateSpellC2SPacket::new)
                .encoder(ActivateSpellC2SPacket::toBytes)
                .consumerMainThread(ActivateSpellC2SPacket::handle)
                .add();

        net.messageBuilder(ActivateShieldC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ActivateShieldC2SPacket::new)
                .encoder(ActivateShieldC2SPacket::toBytes)
                .consumerMainThread(ActivateShieldC2SPacket::handle)
                .add();

        net.messageBuilder(ManaSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaSyncS2CPacket::new)
                .encoder(ManaSyncS2CPacket::toBytes)
                .consumerMainThread(ManaSyncS2CPacket::handle)
                .add();

        net.messageBuilder(ExplosionEffectsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExplosionEffectsS2CPacket::new)
                .encoder(ExplosionEffectsS2CPacket::toBytes)
                .consumerMainThread(ExplosionEffectsS2CPacket::handle)
                .add();

        net.messageBuilder(SpellEntityClientSetupS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpellEntityClientSetupS2CPacket::new)
                .encoder(SpellEntityClientSetupS2CPacket::toBytes)
                .consumerMainThread(SpellEntityClientSetupS2CPacket::handle)
                .add();

        net.messageBuilder(CooldownSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CooldownSyncS2CPacket::new)
                .encoder(CooldownSyncS2CPacket::toBytes)
                .consumerMainThread(CooldownSyncS2CPacket::handle)
                .add();
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
