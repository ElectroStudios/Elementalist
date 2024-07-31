package net.electro.elementalist.networking;

import net.electro.elementalist.util.IEffectSpell;
import net.electro.elementalist.util.IExplosionEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncEffectsS2CPacket {
    private final int entityId;
    private final int targetId;

    public SyncEffectsS2CPacket(int entityId, int targetId) {
        this.entityId = entityId;
        this.targetId = targetId;
    }

    public SyncEffectsS2CPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.targetId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(targetId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            Entity target = Minecraft.getInstance().level.getEntity(targetId);
            if (entity instanceof IEffectSpell effectSpell) {
                effectSpell.effectClient((LivingEntity) target);
            }
        });
        return true;
    };
}
