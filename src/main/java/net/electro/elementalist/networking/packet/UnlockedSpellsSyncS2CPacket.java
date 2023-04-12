package net.electro.elementalist.networking.packet;

import net.electro.elementalist.client.ClientSpellStateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UnlockedSpellsSyncS2CPacket {
    private final int[] unlockedSpells;

    public UnlockedSpellsSyncS2CPacket(List<Integer> unlockedSpells) {
        this.unlockedSpells = unlockedSpells.stream().mapToInt(Integer::intValue).toArray();
    }

    public UnlockedSpellsSyncS2CPacket(FriendlyByteBuf buf) {
        this.unlockedSpells = buf.readVarIntArray();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarIntArray(unlockedSpells);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellStateData.setUnlockedSpells(Arrays.stream(unlockedSpells).boxed().collect(Collectors.toList()));
        });
        return true;
    };
}
