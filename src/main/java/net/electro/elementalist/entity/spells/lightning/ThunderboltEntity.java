package net.electro.elementalist.entity.spells.lightning;

import net.electro.elementalist.registry.EffectRegistry;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.entity.spells.MasterSpellEntity;
import net.electro.elementalist.entity.spells.ShieldSpellEntity;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.SyncEffectsS2CPacket;
import net.electro.elementalist.util.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThunderboltEntity extends MasterSpellEntity implements IEffectSpell {
    private int duration = 6;
    private final float MAX_DISTANCE = 30f;
    private static final EntityDataAccessor<Float> DATA_DISTANCE = SynchedEntityData.defineId(ThunderboltEntity.class, EntityDataSerializers.FLOAT);
    private Vec3 targetPos;
    private MobEffect effect = EffectRegistry.ELECTRIFIED.get();
    public ThunderboltEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThunderboltEntity(LivingEntity owner, DamageType damageType) {
        super(EntityRegistry.THUNDERBOLT.get(), owner, damageType);

        this.setPos(this.position().add(0f, -0.6f, 0f).add(this.getForward().multiply(0.5f, 0.5f, 0.5f)));
        this.setXRot(this.getXRot() + (float) Math.asin((0.6f/ MAX_DISTANCE)));


    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return super.shouldRender(pX, pY, pZ);
    }

    public void setDistance(float distance) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_DISTANCE, distance);
        }
    }

    public float getDistance() {
        return this.getEntityData().get(DATA_DISTANCE);
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (duration == 5) {
                BlockHitResult blockHitResult = this.level().clip(new ClipContext(owner.getEyePosition(),
                        this.position().add(owner.getForward().multiply(MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE)),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

                if (blockHitResult.getType() == HitResult.Type.MISS) {
                    setDistance(MAX_DISTANCE);
                }
                else if (blockHitResult.getType() == HitResult.Type.BLOCK || blockHitResult.getType() == HitResult.Type.ENTITY) {
                    setDistance((float) blockHitResult.getLocation().subtract(this.position()).length());
                }

                this.targetPos = blockHitResult.getLocation();
                RayTraceResult rayTraceResult = Utility.rayTraceEntities(this.level(), getOwner(), owner.getEyePosition(),
                        owner.getForward(), MAX_DISTANCE, 2, entity -> entity instanceof ShieldSpellEntity || entity instanceof LivingEntity);
                if (rayTraceResult.HIT) {
                    double distanceToEntity = this.position().subtract(rayTraceResult.HIT_LOCATION).length();
                    if (getDistance() > distanceToEntity) {
                        setDistance((float)distanceToEntity);
                        this.targetPos = rayTraceResult.HIT_LOCATION;
                        if (rayTraceResult.HIT_ENTITIES.get(0) instanceof LivingEntity entity) {
                            if (this.level().getRandom().nextFloat() < 0.3f) {
                                EffectUtil.addEffect(effect, 60, 1, entity);
                                MessageRegistry.sendToAllPlayers(new SyncEffectsS2CPacket(this.getId(), entity.getId()));
                            }
                            entity.hurt(this.damageSources().indirectMagic(this.getOwner(), this), this.damageType.calculateDamage(this.owner, entity));
                        }
                    }
                }
            }
            else if (duration <= 0) {
                this.discard();
            }
        } else {
            if (duration == 5) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.NEUTRAL, 1f, 1f, false);
            }
        }
        duration--;
    }




    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_DISTANCE, MAX_DISTANCE);
    }

    @Override
    public void effectClient(LivingEntity target) {
        EffectUtil.addEffect(effect, 60, 1, target);
    }
}