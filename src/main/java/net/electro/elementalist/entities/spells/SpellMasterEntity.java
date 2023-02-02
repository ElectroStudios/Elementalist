package net.electro.elementalist.entities.spells;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.util.DamageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpellMasterEntity extends Entity {
    @Nullable
    public LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    protected List<LivingEntity> ignoredEntities;
    protected DamageType damageType;

    public SpellMasterEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SpellMasterEntity(EntityType<?> entityType, LivingEntity owner, DamageType damageType) {
        super(entityType, owner.level);
        setOwner(owner);
        setPos(owner.getEyePosition());
        setRot(owner.yHeadRot, owner.getXRot());
        ignoredEntities = new ArrayList<>();
        ignoredEntities.add(owner);
        this.noPhysics = true;
        this.damageType = damageType;
    }

    @Override
    protected void defineSynchedData() {

    }

    public void setOwner(@javax.annotation.Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    @javax.annotation.Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }

        if (pCompound.contains("IgnoredEntities", 9)) {
            ListTag listtag = pCompound.getList("IgnoredEntities", 10);
            this.ignoredEntities.clear();

            for(int i = 0; i < listtag.size(); ++i) {
                UUID enitityUuid = listtag.getCompound(i).getUUID("UUID");
                ignoredEntities.add((LivingEntity) ((ServerLevel)this.level).getEntity(enitityUuid));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }

        if (!this.ignoredEntities.isEmpty()) {
            ListTag listtag = new ListTag();

            for (LivingEntity entity : this.ignoredEntities) {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("UUID", entity.getUUID());
                listtag.add(tag);
            }

            pCompound.put("IgnoredEntities", listtag);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void clientEntitySetup() {
    }
}
