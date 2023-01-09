package net.electro.elementalist.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DamageDealer {
    protected final Vec3 POS;
    protected final LivingEntity OWNER;
    protected final Entity SOURCE;
    protected final Level LEVEL;
    protected List<LivingEntity> ignoredEntities = new ArrayList<>();
    public DamageDealer(Vec3 pos, LivingEntity owner, Entity source) {
        this.OWNER = owner;
        this.SOURCE = source;
        this.POS = pos;
        this.LEVEL = owner.level;
    }

    public void DamageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
    }

    public void DealDamageSphere(float radius)
    {
        AABB volume = new AABB(POS.x - radius, POS.y - radius, POS.z - radius,
                POS.x + radius * 2, POS.y + radius * 2, POS.z + radius * 2);
        List<LivingEntity> entityList = this.LEVEL.getEntitiesOfClass(LivingEntity.class, volume);
        entityList.removeAll(ignoredEntities);
        if (!entityList.isEmpty()) {
            for (LivingEntity entity : entityList) {
                double distance = POS.distanceTo(entity.position());
                if (distance < radius) {
                    DamageEffects(entity, (float) (1 - distance / radius), getDirectionToEntity(entity));
                }
            }
        }
    }

    protected Vec3 getDirectionToEntity(Entity entity) {
        return POS.subtract(entity.position()).normalize();
    }
}
