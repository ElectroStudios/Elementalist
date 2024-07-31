package net.electro.elementalist.networking;

import net.electro.elementalist.data.ElementalistStats;
import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.registry.SpellRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleCombatModeC2SPacket {
    public ToggleCombatModeC2SPacket() {
    }

    public ToggleCombatModeC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            player.getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(ElementalistStats::toggleCombatMode);
        });
        return true;
    }

}
