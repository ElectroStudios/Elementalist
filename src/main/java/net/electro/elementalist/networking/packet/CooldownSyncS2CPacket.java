package net.electro.elementalist.networking.packet;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CooldownSyncS2CPacket {
    private final int spellId;
    private final int cooldownTicks;

    public CooldownSyncS2CPacket(int spellId, int cooldownTicks) {
        this.spellId = spellId;
        this.cooldownTicks = cooldownTicks;
    }

    public CooldownSyncS2CPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readInt();
        this.cooldownTicks = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(spellId);
        buf.writeInt(cooldownTicks);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setSpellCooldown(spellId, cooldownTicks);
        });
        return true;
    };
}
