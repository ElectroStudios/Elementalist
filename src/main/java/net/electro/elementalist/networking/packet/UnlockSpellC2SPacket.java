package net.electro.elementalist.networking.packet;

import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.ElementalistMaps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UnlockSpellC2SPacket {
    private final int spellId;
    public UnlockSpellC2SPacket (int spellId) {
        this.spellId = spellId;
    }

    public UnlockSpellC2SPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(spellId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            player.getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(elementalistStats -> {
                SpellsMaster spell = ElementalistMaps.spellMap.get(spellId);
                if (!elementalistStats.getUnlockedSpellsList().contains(spellId) && spell.skillPointCost <=
                        elementalistStats.getElementSkillPoints(spell.damageType.ELEMENT)) {
                    elementalistStats.unlockSpell(spellId);
                    elementalistStats.reduceElementSkillPoints(spell.damageType.ELEMENT, spell.skillPointCost);
                    ModMessages.sendToPlayer(new ElementSkillPointsSyncS2CPacket(elementalistStats
                            .getElementSkillPoints(spell.damageType.ELEMENT), ElementalistMaps.elementToIndexMap.get(spell.damageType.ELEMENT)),
                            player);
                    ModMessages.sendToPlayer(new UnlockedSpellsSyncS2CPacket(elementalistStats.getUnlockedSpellsList()),
                            player);
                    player.sendSystemMessage(Component.literal(elementalistStats.getUnlockedSpellsList().toString()));
                }
            });
        });
        return true;
    }

}
