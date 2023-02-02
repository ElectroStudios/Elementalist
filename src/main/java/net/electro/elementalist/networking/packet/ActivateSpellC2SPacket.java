package net.electro.elementalist.networking.packet;

import net.electro.elementalist.item.bracelets.BraceletMaster;
import net.electro.elementalist.util.SpellIdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateSpellC2SPacket {
    private final int spellSlot;
    public ActivateSpellC2SPacket(int spellSlot) {
        this.spellSlot = spellSlot;
    }

    public ActivateSpellC2SPacket(FriendlyByteBuf buf) {
        this.spellSlot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(spellSlot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof BraceletMaster bracelet) {
                if (bracelet.hasNbtData(spellSlot, heldItem)) {
                    int spellId = bracelet.getNbtData(spellSlot, heldItem);
                    SpellIdMap.map.get(spellId).activate(player);
                }
            }
        });
        return true;
    }

}
