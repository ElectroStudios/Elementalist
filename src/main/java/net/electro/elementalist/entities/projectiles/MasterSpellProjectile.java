package net.electro.elementalist.entities.projectiles;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.ExplosionEffectsS2CPacket;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.IExplosionEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

public abstract class MasterSpellProjectile extends Projectile implements IExplosionEffects {
    protected float speed;
    protected DamageType damageType;
    public MasterSpellProjectile(EntityType<? extends MasterSpellProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public MasterSpellProjectile(EntityType<? extends MasterSpellProjectile> type, LivingEntity owner, int speed, DamageType damageType) {
        super(type, owner.level);
        this.speed = speed;
        this.damageType = damageType;
        setOwner(owner);
        setPos(owner.getEyePosition()
                .add(new Vec3(0, -0.3F, 0))
                .add(Vec3.directionFromRotation(owner.getRotationVector()).multiply(1, 1, 1)));
        this.shootProjectileFromRotation(owner.getXRot(), owner.getYRot(), 0.0F, speed, 1.0F);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide())
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
        if (!this.level.isClientSide()) {
            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
        }
        Vec3 vec31 = this.getDeltaMovement();
        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
        if (this.level.isClientSide()) {
            this.projectileTickEffects();
        }
        super.tick();
    }

    public void explodeClient() {
    }

    public void explode() {
        List<ServerPlayer> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (ServerPlayer player : players)
        {
            ModMessages.sendToPlayer(new ExplosionEffectsS2CPacket(this.getId()), player);
        }
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
