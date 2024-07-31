package net.electro.elementalist.networking;

import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.registry.SpellRegistry;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.ElementalistMaps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UnlockSpellC2SPacket {
    private final ResourceLocation spellId;
    public UnlockSpellC2SPacket (ResourceLocation spellId) {
        this.spellId = spellId;
    }

    public UnlockSpellC2SPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(spellId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            player.getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(elementalistStats -> {
                SpellBase spell = SpellRegistry.getSpell(spellId);
                if (!elementalistStats.getUnlockedSpellsList().contains(spellId) && spell.skillPointCost <=
                        elementalistStats.getElementSkillPoints(spell.damageType.ELEMENT)) {
                    elementalistStats.unlockSpell(spellId);
                    elementalistStats.reduceElementSkillPoints(spell.damageType.ELEMENT, spell.skillPointCost);
                    MessageRegistry.sendToPlayer(new ElementSkillPointsSyncS2CPacket(elementalistStats
                            .getElementSkillPoints(spell.damageType.ELEMENT), ElementalistMaps.elementToIndexMap.get(spell.damageType.ELEMENT)),
                            player);
                    MessageRegistry.sendToPlayer(new UnlockedSpellsSyncS2CPacket(elementalistStats.getUnlockedSpellsList()),
                            player);
                    player.sendSystemMessage(Component.literal(elementalistStats.getUnlockedSpellsList().toString()));
                }
            });
        });
        return true;
    }

}
