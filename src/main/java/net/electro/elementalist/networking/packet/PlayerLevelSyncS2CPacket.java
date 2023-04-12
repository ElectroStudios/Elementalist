package net.electro.elementalist.networking.packet;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerLevelSyncS2CPacket {
    private final int playerLevel;

    public PlayerLevelSyncS2CPacket(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public PlayerLevelSyncS2CPacket(FriendlyByteBuf buf) {
        this.playerLevel = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(playerLevel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setPlayerLevel(playerLevel);
        });
        return true;
    };
}
