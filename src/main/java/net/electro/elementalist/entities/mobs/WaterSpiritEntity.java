package net.electro.elementalist.entities.mobs;

import net.electro.elementalist.entities.mobs.goals.SpellAttackOffensiveGoal;
import net.electro.elementalist.entities.mobs.goals.SpellEvadeGoal;
import net.electro.elementalist.spells.lightning.ThunderboltSpell;
import net.electro.elementalist.spells.water.WaterSlashSpell;
import net.electro.elementalist.spells.water.WaterStreamSpell;
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

public class WaterSpiritEntity extends ElementalistMob implements GeoEntity {
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WaterSpiritEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.maxMana = 100;
        this.availableSpells = List.of(new WaterSlashSpell(), new WaterStreamSpell(), new ThunderboltSpell());
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.4f)
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

        state.getController().setAnimation(RawAnimation.begin().then("animation.water_spirit.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
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
