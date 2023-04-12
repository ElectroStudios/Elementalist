package net.electro.elementalist.networking.packet;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ElementSkillPointsSyncS2CPacket {
    private final int element;
    private final int elementSkillPoints;

    public ElementSkillPointsSyncS2CPacket(int elementSkillPoints, int element) {
        this.elementSkillPoints = elementSkillPoints;
        this.element = element;
    }

    public ElementSkillPointsSyncS2CPacket(FriendlyByteBuf buf) {
        this.elementSkillPoints = buf.readInt();
        this.element = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(elementSkillPoints);
        buf.writeInt(element);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setElementSkillPoints(elementSkillPoints, element);
        });
        return true;
    };
}
