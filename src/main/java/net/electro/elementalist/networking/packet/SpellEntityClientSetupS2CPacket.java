package net.electro.elementalist.networking.packet;

import net.electro.elementalist.entities.spells.MasterSpellEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SpellEntityClientSetupS2CPacket {
    private final UUID entity;

    public SpellEntityClientSetupS2CPacket(UUID entity) {
        this.entity = entity;
    }

    public SpellEntityClientSetupS2CPacket(FriendlyByteBuf buf) {
        this.entity = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(entity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ((MasterSpellEntity)((ServerLevel)player.level()).getEntity(entity)).clientEntitySetup();
        });
        return true;
    };
}
