package net.electro.elementalist.util;

import net.electro.elementalist.entity.spells.ShieldSpellEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.List;

public class DamageDealer {
    protected final Vec3 POS;
    protected final LivingEntity OWNER;
    protected final Entity SOURCE;
    protected final Level LEVEL;
    protected final List<LivingEntity> IGNORED_ENTITIES;
    protected final DamageType DAMAGE_TYPE;
    public DamageDealer(Vec3 pos, LivingEntity owner, Entity source, List<LivingEntity> ignoredEntities, DamageType damageType) {
        this.OWNER = owner;
        this.SOURCE = source;
        this.POS = pos;
        this.LEVEL = source.level();
        this.IGNORED_ENTITIES = ignoredEntities;
        this.DAMAGE_TYPE = damageType;
    }

    public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
        entity.hurt(OWNER.damageSources().indirectMagic(this.SOURCE, this.OWNER), this.DAMAGE_TYPE.calculateDamage(this.OWNER, entity));
        entity.knockback(effectAmount * this.DAMAGE_TYPE.KNOCKBACK, direction.x, direction.z);
    }

    public boolean checkForLineOfSight(Entity entity) {
        BlockHitResult blockHitResult1 = this.LEVEL.clip(new ClipContext(POS, entity.position(),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.SOURCE));
        BlockHitResult blockHitResult2 = this.LEVEL.clip(new ClipContext(POS, entity.getEyePosition(),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.SOURCE));
        return blockHitResult1.getType() == HitResult.Type.MISS || blockHitResult2.getType() == HitResult.Type.MISS;
    }

    public void setBlocksInRadius(int radius, float probability) {
        List<BlockPos> blocks = new ArrayList<>();
        BlockPos blockPos = new BlockPos((int)POS.x, (int)POS.y, (int)POS.z);

        for (int aX = -radius; aX <= radius; aX++) {
            for (int aY = -radius; aY <= radius; aY++) {
                for (int aZ = -radius; aZ <= radius; aZ++) {
                    blocks.add(blockPos.offset(aX, aY, aZ));
                }
            }
        }

        for(BlockPos pos : blocks) {
            if (this.LEVEL.getRandom().nextFloat() <= probability && this.LEVEL.getBlockState(pos).isAir()
                    && this.LEVEL.getBlockState(pos.below()).isSolidRender(this.LEVEL, pos.below())) {
                this.LEVEL.setBlockAndUpdate(pos, BaseFireBlock.getState(this.LEVEL, pos));
            }
        }
    }

    public void dealDamageSphere(float radius, float height)
    {
        AABB volume = new AABB(POS.x - radius, POS.y - height, POS.z - radius,
                POS.x + radius, POS.y + height, POS.z + radius);
        List<LivingEntity> entityList = this.LEVEL.getEntitiesOfClass(LivingEntity.class, volume);
        List<ShieldSpellEntity> shieldEntityList = this.LEVEL.getEntitiesOfClass(ShieldSpellEntity.class, volume);
        entityList.removeAll(IGNORED_ENTITIES);
        if (!shieldEntityList.isEmpty()) {
            for (ShieldSpellEntity entity : shieldEntityList) {
                double distance = POS.distanceTo(entity.position());
                if (distance < radius && checkForLineOfSight(entity)) {
                    if (!entity.calculateWinningSide(this.DAMAGE_TYPE)) {
                        SOURCE.discard();
                        return;
                    }
                }
            }
        }
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
        List<ShieldSpellEntity> shieldEntityList = this.LEVEL.getEntitiesOfClass(ShieldSpellEntity.class, volume);
        entityList.removeAll(IGNORED_ENTITIES);
        if (!shieldEntityList.isEmpty()) {
            for (ShieldSpellEntity entity : shieldEntityList) {
                    double distance = POS.distanceTo(entity.position());
                    Vec3 directionToEntity = getDirectionToEntity(entity);
                    if (isEntityInCone(entity, angle, radius, distance, directionToEntity))
                    {
                        if (!entity.calculateWinningSide(DAMAGE_TYPE))
                        {
                            SOURCE.discard();
                            return;
                        }
                    }
                }
            }
        if (!entityList.isEmpty()) {
            for (LivingEntity entity : entityList) {
                double distance = POS.distanceTo(entity.position());
                Vec3 directionToEntity = getDirectionToEntity(entity);
                if (isEntityInCone(entity, angle, radius, distance, directionToEntity)) {
                    damageEffects(entity, (float) (1 - distance / radius), directionToEntity);
                }
            }
        }
    }

    public boolean isEntityInCone(Entity entity, float angle, float radius, double distance, Vec3 directionToEntity) {
        Vec3 spellDirection = SOURCE.getForward();
        double angleToEntity = 180 - Math.toDegrees(Math.acos(spellDirection.dot(directionToEntity) / (spellDirection.length() * directionToEntity.length())));
        return distance < radius && angleToEntity <= angle && checkForLineOfSight(entity);
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

//    public void dealDamageRaytrace(Vec3 targetPos) {
//        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.OWNER, this.POS, targetPos,
//                new AABB(this.POS, targetPos).inflate(5f), (entity) -> {return !entity.isSpectator() &&
//                        entity.isPickable() && !IGNORED_ENTITIES.contains(entity) && entity instanceof LivingEntity;
//        }, this.POS.subtract(targetPos).length());
//        if (entityHitResult != null)
//        {
//            damageEffects((LivingEntity) entityHitResult.getEntity(), 1f, this.SOURCE.getForward().yRot(90));
//        }
//    }

    protected Vec3 getDirectionToEntity(Entity entity) {
        return POS.subtract(entity.position()).normalize();
    }
}
