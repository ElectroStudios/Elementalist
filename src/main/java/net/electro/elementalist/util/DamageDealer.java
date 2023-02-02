package net.electro.elementalist.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DamageDealer {
    protected final Vec3 POS;
    protected final LivingEntity OWNER;
    protected final Entity SOURCE;
    protected final Level LEVEL;
    protected final List<LivingEntity> IGNORED_ENTITIES;
    public DamageDealer(Vec3 pos, LivingEntity owner, Entity source, List<LivingEntity> ignoredEntities) {
        this.OWNER = owner;
        this.SOURCE = source;
        this.POS = pos;
        this.LEVEL = owner.level;
        this.IGNORED_ENTITIES = ignoredEntities;
    }

    public DamageDealer(Vec3 pos, LivingEntity owner, Entity source) {
        this.OWNER = owner;
        this.SOURCE = source;
        this.POS = pos;
        this.LEVEL = owner.level;
        this.IGNORED_ENTITIES = new ArrayList<>();
    }

    public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
    }

    public boolean checkForLineOfSight(LivingEntity entity) {
        BlockHitResult blockHitResult1 = this.LEVEL.clip(new ClipContext(POS, entity.position(),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.SOURCE));
        BlockHitResult blockHitResult2 = this.LEVEL.clip(new ClipContext(POS, entity.getEyePosition(),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.SOURCE));
        return blockHitResult1.getType() == HitResult.Type.MISS || blockHitResult2.getType() == HitResult.Type.MISS;
    }

    public void dealDamageSphere(float radius, float height)
    {
        AABB volume = new AABB(POS.x - radius, POS.y - height, POS.z - radius,
                POS.x + radius, POS.y + height, POS.z + radius);
        List<LivingEntity> entityList = this.LEVEL.getEntitiesOfClass(LivingEntity.class, volume);
        entityList.removeAll(IGNORED_ENTITIES);
        if (!entityList.isEmpty()) {
            for (LivingEntity entity : entityList) {
                double distance = POS.distanceTo(entity.position());
                if (distance < radius && checkForLineOfSight(entity)) {
                    damageEffects(entity, (float) (1 - distance / radius), getDirectionToEntity(entity));
                }
            }
        }
    }

    public void dealDamageCone(float angle, float radius) {
        AABB volume = new AABB(POS.x - radius, POS.y - radius, POS.z - radius,
                POS.x + radius, POS.y + radius, POS.z + radius);
        List<LivingEntity> entityList = this.LEVEL.getEntitiesOfClass(LivingEntity.class, volume);
        entityList.removeAll(IGNORED_ENTITIES);
        if (!entityList.isEmpty()) {
            for (LivingEntity entity : entityList) {
                double distance = POS.distanceTo(entity.position());
                Vec3 spellDirection = Vec3.directionFromRotation(SOURCE.getRotationVector());
                Vec3 directionToEntity = getDirectionToEntity(entity);
                double angleToEntity = 180 - Math.toDegrees(Math.acos(spellDirection.dot(directionToEntity)/(spellDirection.length() * directionToEntity.length())));
                if (distance < radius && angleToEntity <= angle && checkForLineOfSight(entity)) {
                    damageEffects(entity, (float) (1 - distance / radius), directionToEntity);
                }
            }
        }
    }

    public void dealDamageTube(float radius, float distance, Vec3 knockbackDirection) {
        AABB volume = new AABB(POS.x - distance, POS.y - distance, POS.z - distance,
                POS.x + distance, POS.y + distance, POS.z + distance);
        List<LivingEntity> entityList = this.LEVEL.getEntitiesOfClass(LivingEntity.class, volume);
        entityList.removeAll(IGNORED_ENTITIES);
        if (!entityList.isEmpty()) {
            for (LivingEntity entity : entityList) {
                double distanceToTubeVector = Utility.getDistanceFromPointToVector(SOURCE.getForward(), POS, entity.position());
                double distanceToEntity = POS.distanceTo(entity.position());
                if (distanceToTubeVector <= radius && distanceToEntity <= distance) {
                    damageEffects(entity, (float) distanceToTubeVector / radius, knockbackDirection.reverse());
                }
            }
        }
    }

    public void dealDamageRaytrace(Vec3 targetPos) {
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.OWNER, this.POS, targetPos,
                new AABB(this.POS, targetPos).inflate(5f), (entity) -> {return !entity.isSpectator() &&
                        entity.isPickable() && !IGNORED_ENTITIES.contains(entity) && entity instanceof LivingEntity;
        }, this.POS.subtract(targetPos).length());
        if (entityHitResult != null)
        {
            damageEffects((LivingEntity) entityHitResult.getEntity(), 1f, this.OWNER.getForward().yRot(90));
        }
    }

    protected Vec3 getDirectionToEntity(Entity entity) {
        return POS.subtract(entity.position()).normalize();
    }
}
