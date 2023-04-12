package net.electro.elementalist.entities.spells;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.projectiles.MasterSpellProjectile;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.ExplosionEffectsS2CPacket;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.IExplosionEffects;
import net.electro.elementalist.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

public class ShieldSpellEntity extends Entity implements IAnimatable, IExplosionEffects {
    @Nullable
    public LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private int duration = 100;
    private float shieldStrength = 11;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public ShieldSpellEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public ShieldSpellEntity(LivingEntity owner) {
        super(ModEntities.SHIELD_SPELL.get(), owner.level);
        setOwner(owner);
        setPos(owner.getEyePosition().add(owner.getForward().multiply(2, 2, 2)).subtract(0, 1.5f, 0));
        setRot(owner.yHeadRot, owner.getXRot());
        this.noPhysics = true;
        this.level.addFreshEntity(new MagicCircleEntity(this.position(), this.getYRot(), this.getXRot(), this.level,
                100, 1f, true, 0xFFFFB1A8));

        this.level.addFreshEntity(new MagicCircleEntity(this.position().add(this.getForward()), this.getYRot(),
                this.getXRot(), this.level, 100, 1f, false, 0xFFFFB1A8));
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide()) {
            Vec3 particlePos = this.position().add(0, 1.5f, 0);
            if (duration == 90 || duration == 88) {
                ParticleUtil.createParticleCircle(ParticleTypes.FLAME, this.level, 0.8f, particlePos, this.getYRot(), this.getXRot(), 2);
            }
            else if (duration == 86) {
                ParticleUtil.createParticleCircle(ParticleTypes.END_ROD, this.level, 0.8f, particlePos, this.getYRot(), this.getXRot(), 2);
            }
        }
        else {
            List<Projectile> overlappingProjectiles = this.level.getEntitiesOfClass(Projectile.class, this.getBoundingBox(), (projectile -> {return true;}));
            if (!overlappingProjectiles.isEmpty())
            {
                for (Projectile projectile : overlappingProjectiles) {
                    handleHitProjectile(projectile);
                }
            }
            if (duration <= 0) {
                this.discard();
            }
        }
        duration--;
    }

    private void handleHitProjectile(Projectile projectile) {
        if (projectile instanceof MasterSpellProjectile masterSpellProjectile) {
            if (!this.level.isClientSide()) {
                if (!calculateWinningSide(masterSpellProjectile.damageType)) {
                    masterSpellProjectile.explode();
                }
            }
        }
    }

    public boolean calculateWinningSide(DamageType damageType) {
        if (damageType.BASE_DAMAGE >= shieldStrength)
        {
            ModMessages.sendToAllPlayers(new ExplosionEffectsS2CPacket(this.getId()));
            this.discard();
            return true;
        }
        else {
            ModMessages.sendToAllPlayers(new ExplosionEffectsS2CPacket(this.getId()));
            return false;
        }
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
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.spell_shield.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void explodeClient() {
        ParticleUtil.createParticleCircle(ParticleTypes.END_ROD, this.level, 0.8f,
                this.position().add(0, 1.5f, 0), this.getYRot(), this.getXRot(), 2);
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_DESTROY,
                SoundSource.NEUTRAL, 2f, 1f, false);
    }
}
