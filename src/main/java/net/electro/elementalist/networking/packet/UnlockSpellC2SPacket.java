package net.electro.elementalist.networking.packet;

import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.item.bracelets.BraceletMaster;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UnlockSpellC2SPacket {
    public UnlockSpellC2SPacket () {

    }

    public UnlockSpellC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            player.getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(elementalistStats -> {
                elementalistStats.unlockSpell(0);
                player.sendSystemMessage(Component.literal("Unlocked Spells: " + elementalistStats.getSpellsString())
                        .withStyle(ChatFormatting.AQUA));
            });
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof BraceletMaster bracelet) {
                bracelet.addNbtData(0, 0, heldItem);
                bracelet.addNbtData(1, 2, heldItem);
            }

        });
        return true;
    }

}
