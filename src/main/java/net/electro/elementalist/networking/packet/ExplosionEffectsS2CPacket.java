package net.electro.elementalist.networking.packet;

import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.util.IExplosionEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExplosionEffectsS2CPacket {
    private final int entityId;

    public ExplosionEffectsS2CPacket(int entityId) {
        this.entityId = entityId;
    }

    public ExplosionEffectsS2CPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (entity instanceof IExplosionEffects explosionEntity) {
                explosionEntity.explodeClient();
            }
        });
        return true;
    };
}
