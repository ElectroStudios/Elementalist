package net.electro.elementalist.data;

import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.CooldownSyncS2CPacket;
import net.electro.elementalist.networking.ManaSyncS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.HashMap;
import java.util.Map;


@AutoRegisterCapability
public class SpellState {
    private int mana = 0;
    private Map<ResourceLocation, Integer> spellCooldowns = new HashMap<ResourceLocation, Integer>();
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
        MessageRegistry.sendToPlayer(new ManaSyncS2CPacket(this.mana), (ServerPlayer) player);
    }

    public void subMana (int amount, Player player) {
        this.mana = Math.max(mana - amount, 0);
        MessageRegistry.sendToPlayer(new ManaSyncS2CPacket(this.mana), (ServerPlayer) player);
    }

    public void addSpellCooldown(ResourceLocation spellId, int cooldownTicks, Player player) {
        spellCooldowns.put(spellId, cooldownTicks);
        MessageRegistry.sendToPlayer(new CooldownSyncS2CPacket(spellId, cooldownTicks), (ServerPlayer) player);
    }

    public void decreaseSpellCooldowns() {
        spellCooldowns.replaceAll((k, v) -> Math.max(v - 5, 0));
    }

    public int getSpellCooldown(ResourceLocation spellId) {
        return (spellCooldowns.get(spellId) == null ? 0 : spellCooldowns.get(spellId));
    }

    public boolean isSpellReady(ResourceLocation spellId) {
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
