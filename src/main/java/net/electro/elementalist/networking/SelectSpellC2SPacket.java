package net.electro.elementalist.networking;

import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SelectSpellC2SPacket {
    private final ResourceLocation spellId;
    private final int slotId;
    public SelectSpellC2SPacket(ResourceLocation spellId, int slotId) {
        this.spellId = spellId;
        this.slotId = slotId;
    }

    public SelectSpellC2SPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readResourceLocation();
        this.slotId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(spellId);
        buf.writeInt(slotId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof ChargedStaff bracelet) {
                bracelet.addNbtData(slotId, spellId, heldItem);
            }

        });
        return true;
    }

}
