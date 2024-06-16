package net.electro.elementalist.networking.packet;

import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.spells.PlayerMovementManager;
import net.electro.elementalist.util.Utility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MovementSkillInputC2SPacket {
    private final String skillName;
    public MovementSkillInputC2SPacket(String skillName) {
        this.skillName = skillName;
    }

    public MovementSkillInputC2SPacket(FriendlyByteBuf buf) {
        this.skillName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(skillName);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            PlayerMovementManager movementManager = new PlayerMovementManager(player);
            movementManager.performMovementAction(skillName);
        });
        return true;
    }

}

