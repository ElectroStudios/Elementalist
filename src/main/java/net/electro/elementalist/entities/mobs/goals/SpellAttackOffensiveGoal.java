package net.electro.elementalist.entities.mobs.goals;

import java.util.EnumSet;

import net.electro.elementalist.entities.mobs.ElementalistMob;
import net.electro.elementalist.spells.SpellMaster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class SpellAttackOffensiveGoal<T extends net.minecraft.world.entity.Mob & RangedAttackMob> extends Goal {
    private final ElementalistMob mob;
    private float attackRadius;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    public SpellMaster preferredSpell;
    public int manaCostAim;

    public SpellAttackOffensiveGoal(ElementalistMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return this.mob.getTarget() != null;
    }


    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return (this.canUse() && !this.mob.getNavigation().isDone());
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        super.start();
        this.mob.setAggressive(true);
        manaCostAim = this.mob.currentMana;
    }

    public void setPreferredSpell() {
        LivingEntity target = this.mob.getTarget();
        double distanceToTarget = this.mob.distanceTo(target);
        double maxSpellPreference = 0f;
        for (SpellMaster spell : this.mob.availableSpells) {
            double spellPreference = (Math.min(1, spell.range / distanceToTarget) * 30 +
                    (1 - Math.abs(spell.manaCost - manaCostAim) / (double)this.mob.maxMana) * 100 -
                    this.mob.getSpellCooldown(spell.spellId) * 0.5 -
                    Math.max(0, spell.manaCost - this.mob.currentMana) * 0.5) / this.mob.getSpellUsedAmount(spell.spellId);
            if (spellPreference > maxSpellPreference) {
                maxSpellPreference = spellPreference;
                preferredSpell = spell;
                attackRadius = spell.range;
            }
        }
    }

    public boolean canUseSpell(SpellMaster spell) {
        return this.mob.isSpellReady(spell.spellId) && this.mob.currentMana >= spell.manaCost;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
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
            double distanceToTarget = this.mob.distanceTo(target);
            boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(target);
            setPreferredSpell();



            if (distanceToTarget <= (double)this.attackRadius) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
//                if (this.mob.getNavigation().getPath() != null &&
//                        this.mob.level.getBlockState(this.mob.getNavigation().getPath().getNextNodePos()).getBlock().equals(Blocks.FIRE)) {
//                    this.mob.getNavigation().recomputePath();
//                } else {
                    this.mob.getNavigation().moveTo(target, 1);
//                }

                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distanceToTarget > (double)(this.attackRadius * 0.9F)) {
                    this.strafingBackwards = false;
                } else if (distanceToTarget < (double)(this.attackRadius * 0.4F)) {
                    this.strafingBackwards = true;
                }

                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.lookAt(target, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (hasLineOfSight && distanceToTarget <= attackRadius * 0.9 && this.mob.attackInterval == 0 && canUseSpell(preferredSpell)) {
                this.mob.lookAt(target, 90, 90);
                this.mob.activateSpell(preferredSpell);
                manaCostAim = this.mob.getRandom().nextInt(this.mob.maxMana);
            }

        }
    }
}
