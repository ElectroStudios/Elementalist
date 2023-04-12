package net.electro.elementalist.networking.packet;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ElementExperienceSyncS2CPacket {
    private final int element;
    private final int elementExperience;

    public ElementExperienceSyncS2CPacket(int elementExperience, int element) {
        this.elementExperience = elementExperience;
        this.element = element;
    }

    public ElementExperienceSyncS2CPacket(FriendlyByteBuf buf) {
        this.elementExperience = buf.readInt();
        this.element = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(elementExperience);
        buf.writeInt(element);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setElementExperience(elementExperience, element);
        });
        return true;
    };
}
