package net.electro.elementalist.entity.mobs.goals;

import net.electro.elementalist.entity.mobs.ElementalistMob;
import net.electro.elementalist.entity.projectiles.MasterSpellProjectile;
import net.electro.elementalist.entity.spells.ShieldSpellEntity;
import net.electro.elementalist.entity.spells.MasterSpellEntity;
import net.electro.elementalist.util.Utility;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class SpellEvadeGoal<T extends net.minecraft.world.entity.Mob & RangedAttackMob> extends Goal {
    private final ElementalistMob mob;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private MasterSpellEntity spellEntity;
    private MasterSpellProjectile spellProjectile;
    private int evadeType;


    public SpellEvadeGoal(ElementalistMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }


    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        List<MasterSpellEntity> spellsInArea = this.mob.level().getEntitiesOfClass(MasterSpellEntity.class,
                new AABB(this.mob.position().subtract(50, 50, 50),
                this.mob.position().add(50, 50, 50)));
        List<MasterSpellProjectile> projectilesInArea = this.mob.level().getEntitiesOfClass(MasterSpellProjectile.class,
                new AABB(this.mob.position().subtract(50, 50, 50),
                        this.mob.position().add(50, 50, 50)));
        for (MasterSpellEntity spell : spellsInArea) {
            if (spell.getOwner() != this.mob) {
                double distance = Utility.getDistanceFromPointToVector(spell.getForward(), spell.position(), this.mob.position());
                if (distance <= 5) {
                    spellEntity = spell;
                    return true;
                }
            }
        }
        for (MasterSpellProjectile projectile : projectilesInArea) {
            if (projectile.getOwner() != this.mob) {
                double distance = Utility.getDistanceFromPointToVector(projectile.getForward(), projectile.position(), this.mob.position());
                if (distance <= 5) {
                    spellProjectile = projectile;
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.canUse();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        super.start();
        if (this.mob.getRandom().nextFloat() < 0.3f) {
            evadeType = 0;
        } else if (this.mob.getRandom().nextFloat() < 0.4f) {
            evadeType = 1;
        } else {
            evadeType = 2;
        }

        if (evadeType == 0) {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                this.mob.lookAt(target, 90, 90);
            }
            ShieldSpellEntity entity = new ShieldSpellEntity(this.mob);
            this.mob.level().addFreshEntity(entity);
        }
    }


    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop();

    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {

            if (evadeType == 1) {
                this.mob.getMoveControl().strafe(0f, 1f);
                this.mob.lookAt(target, 30.0F, 30.0F);
            }



        }
    }
}
