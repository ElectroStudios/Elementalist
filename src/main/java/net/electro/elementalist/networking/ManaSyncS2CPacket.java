package net.electro.elementalist.networking;

import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.data.ElementalistStatsProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaSyncS2CPacket {
    private final int mana;

    public ManaSyncS2CPacket(int mana) {
        this.mana = mana;
    }

    public ManaSyncS2CPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(mana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setMana(mana);
        });
        return true;
    };
}
