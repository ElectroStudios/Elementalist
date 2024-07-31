package net.electro.elementalist.networking;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ElementLevelSyncS2CPacket {
    private final int element;
    private final int elementLevel;

    public ElementLevelSyncS2CPacket(int elementLevel, int element) {
        this.elementLevel = elementLevel;
        this.element = element;
    }

    public ElementLevelSyncS2CPacket(FriendlyByteBuf buf) {
        this.elementLevel = buf.readInt();
        this.element = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(elementLevel);
        buf.writeInt(element);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setElementLevel(elementLevel, element);
        });
        return true;
    };
}
