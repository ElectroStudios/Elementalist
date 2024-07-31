package net.electro.elementalist.networking;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UnlockedSpellsSyncS2CPacket {
    private final List<ResourceLocation> unlockedSpells;

    public UnlockedSpellsSyncS2CPacket(List<ResourceLocation> unlockedSpells) {
        this.unlockedSpells = unlockedSpells;
    }

    public UnlockedSpellsSyncS2CPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        unlockedSpells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            unlockedSpells.add(buf.readResourceLocation());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(unlockedSpells.size());
        for (ResourceLocation spellId : unlockedSpells) {
            buf.writeResourceLocation(spellId);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setUnlockedSpells(unlockedSpells);
        });
        return true;
    };
}
