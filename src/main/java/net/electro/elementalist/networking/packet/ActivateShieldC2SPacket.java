package net.electro.elementalist.networking.packet;

import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.electro.elementalist.item.bracelets.BraceletMaster;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateShieldC2SPacket {
    public ActivateShieldC2SPacket() {    }

    public ActivateShieldC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof BraceletMaster bracelet) {
                ShieldSpellEntity entity = new ShieldSpellEntity(player);
                level.addFreshEntity(entity);
            }
        });
        return true;
    }

}
