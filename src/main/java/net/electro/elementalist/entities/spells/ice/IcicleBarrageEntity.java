package net.electro.elementalist.entities.spells.ice;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.projectiles.IceSpearProjectile;
import net.electro.elementalist.entities.spells.MasterSpellEntity;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Utility;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IcicleBarrageEntity extends MasterSpellEntity {
    private int duration = 20;
    public IcicleBarrageEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IcicleBarrageEntity(LivingEntity owner, DamageType damageType) {
        super(ModEntities.ICICLE_BARRAGE.get(), owner, damageType);

    }


    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (duration % 3 == 0) {
                Vec3 position = Utility.getRandomVectorSphere(this.level().getRandom(), 2.5f, true).add(this.position());
                IceSpearProjectile iceSpear = new IceSpearProjectile(this.getOwner(), damageType);
                iceSpear.setPos(position);
                this.level().addFreshEntity(iceSpear);
            }
            if (duration <= 0) {
                this.discard();
            }
        }

        duration--;
    }
}