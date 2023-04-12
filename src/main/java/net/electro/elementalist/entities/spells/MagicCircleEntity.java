package net.electro.elementalist.entities.spells;

import net.electro.elementalist.entities.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MagicCircleEntity extends Entity implements IAnimatable {
    protected int duration;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final boolean CLOCKWISE;
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(MagicCircleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ANIMATION_SPEED = SynchedEntityData.defineId(MagicCircleEntity.class, EntityDataSerializers.FLOAT);

    public MagicCircleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.duration = 100;
        this.CLOCKWISE = false;
    }

    public MagicCircleEntity(Vec3 position, float yRot, float xRot, Level level, int duration, float animationSpeed, boolean clockwise, int color) {
        super(ModEntities.MAGIC_CIRCLE.get(), level);
        setPos(position);
        setRot(yRot, xRot);
        this.duration = duration;
        this.noPhysics = true;
        this.CLOCKWISE = clockwise;
        setAnimationSpeed(animationSpeed);
        setColor(color);
    }

    public void setColor(int color) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_COLOR, color);
        }
    }

    public int getColor() {
        return this.getEntityData().get(DATA_COLOR);
    }

    public void setAnimationSpeed(float animationSpeed) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_ANIMATION_SPEED, animationSpeed);
        }
    }

    public float getAnimationSpeed() {
        return this.getEntityData().get(DATA_ANIMATION_SPEED);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_COLOR, 0xFFFFFFFF);
        this.getEntityData().define(DATA_ANIMATION_SPEED, 1f);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return pDistance < 10000;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            if (duration <= 0) {
                this.discard();
            }
        }
        duration--;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animationName;
        if (CLOCKWISE) {
            animationName = "animation.magic_circle.clockwise";
        }
        else {
            animationName = "animation.magic_circle.counterclockwise";
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName));
        event.getController().setAnimationSpeed(getAnimationSpeed());
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
