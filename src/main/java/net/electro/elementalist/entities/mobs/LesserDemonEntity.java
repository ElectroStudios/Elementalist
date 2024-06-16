package net.electro.elementalist.entities.mobs;

import net.electro.elementalist.entities.mobs.goals.SpellAttackOffensiveGoal;
import net.electro.elementalist.entities.mobs.goals.SpellEvadeGoal;
import net.electro.elementalist.spells.fire.FireBallSpell;
import net.electro.elementalist.spells.fire.FireBreathSpell;
import net.electro.elementalist.spells.fire.FirePulseSpell;
import net.electro.elementalist.spells.fire.FireWaveSpell;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


import java.util.List;

public class LesserDemonEntity extends ElementalistMob implements GeoEntity {
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LesserDemonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.maxMana = 150;
        this.availableSpells = List.of(new FireBreathSpell(), new FireWaveSpell(), new FirePulseSpell(), new FireBallSpell());
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SpellEvadeGoal<>(this));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new SpellAttackOffensiveGoal<>(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    private PlayState predicate(AnimationState state) {

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.lesser_demon.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        state.getController().setAnimation(RawAnimation.begin().then("animation.lesser_demon.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }


    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
