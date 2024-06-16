package net.electro.elementalist.data;

import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.CooldownSyncS2CPacket;
import net.electro.elementalist.networking.packet.ManaSyncS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;


@AutoRegisterCapability
public class SpellState {
    private int mana = 0;
    private Map<Integer, Integer> spellCooldowns = new HashMap<Integer, Integer>();
    private int MAX_MANA = 100;
    private int jumpsLeft = 2;
    private boolean fallProtection;
    private Vec3 savedDeltaMovement;

    public void setFallProtection (boolean value) {
        this.fallProtection = value;
    }

    public boolean getFallProtection () {
        return fallProtection;
    }

    public void setJumpsLeft (int value) {
        this.jumpsLeft = value;
    }

    public boolean isJumpAvailable () {
        return jumpsLeft > 0;
    }

    public void decrementJumpsAvailable () {
        this.jumpsLeft = Math.max(jumpsLeft - 1, 0);
    }

    public void setSavedDeltaMovement (Vec3 value) {
        this.savedDeltaMovement = value;
    }

    public Vec3 getSavedDeltaMovement () {
        return savedDeltaMovement;
    }

    public void addMana (int amount, Player player) {
        this.mana = Math.min(amount + mana, MAX_MANA);
        ModMessages.sendToPlayer(new ManaSyncS2CPacket(this.mana), (ServerPlayer) player);
    }

    public void subMana (int amount, Player player) {
        this.mana = Math.max(mana - amount, 0);
        ModMessages.sendToPlayer(new ManaSyncS2CPacket(this.mana), (ServerPlayer) player);
    }

    public void addSpellCooldown(int spellId, int cooldownTicks, Player player) {
        spellCooldowns.put(spellId, cooldownTicks);
        ModMessages.sendToPlayer(new CooldownSyncS2CPacket(spellId, cooldownTicks), (ServerPlayer) player);
    }

    public void decreaseSpellCooldowns() {
        spellCooldowns.replaceAll((k, v) -> Math.max(v - 5, 0));
    }

    public int getSpellCooldown(int spellId) {
        return (spellCooldowns.get(spellId) == null ? 0 : spellCooldowns.get(spellId));
    }

    public boolean isSpellReady(int spellId) {
        return spellCooldowns.get(spellId) == null || spellCooldowns.get(spellId) == 0;
    }

    public int getMana ()
    {
        return mana;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("mana", mana);
        spellCooldowns.forEach((k, v) -> nbt.putInt("spell_cooldown_" + k, v));
    }

    public void loadNBTData(CompoundTag nbt)
    {
        mana = nbt.getInt("mana");
        spellCooldowns.replaceAll((k, v) -> nbt.getInt("spell_cooldown_" + k));
    }
}
