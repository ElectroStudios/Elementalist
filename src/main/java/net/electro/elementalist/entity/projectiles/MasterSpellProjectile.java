package net.electro.elementalist.entity.projectiles;

import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.ExplosionEffectsS2CPacket;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.IExplosionEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class MasterSpellProjectile extends Projectile implements IExplosionEffects {
    protected float speed;
    public DamageType damageType;
    protected List<LivingEntity> ignoredEntities;
    public MasterSpellProjectile(EntityType<? extends MasterSpellProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public MasterSpellProjectile(EntityType<? extends MasterSpellProjectile> type, LivingEntity owner, float speed, DamageType damageType) {
        super(type, owner.level());
        this.speed = speed;
        this.damageType = damageType;
        setOwner(owner);
        ignoredEntities = new ArrayList<>();
        this.ignoredEntities.add(owner);
        setPos(owner.getEyePosition()
                .add(new Vec3(0, -0.3F, 0))
                .add(Vec3.directionFromRotation(owner.getRotationVector()).multiply(1, 1, 1)));
        this.shootProjectileFromRotation(owner.getXRot(), owner.getYRot(), 0.0F, speed, 1.0F);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide())
        {
            this.explode();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
    }

    protected void projectileTickEffects() {
    }
    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
        }
        Vec3 vec31 = this.getDeltaMovement();
        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
        if (this.level().isClientSide()) {
            this.projectileTickEffects();
        }
        super.tick();
    }

    public void explodeClient() {
    }

    public void explode() {
        MessageRegistry.sendToAllPlayers(new ExplosionEffectsS2CPacket(this.getId()));
        this.discard();
    }

    public void shootProjectileFromRotation(float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
    }

    @Override
    protected void defineSynchedData() {
    }
}
