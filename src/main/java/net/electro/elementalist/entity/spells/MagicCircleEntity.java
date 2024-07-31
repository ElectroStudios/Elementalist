package net.electro.elementalist.entity.spells;

import net.electro.elementalist.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


public class MagicCircleEntity extends Entity implements GeoEntity {
    protected int duration;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
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
        super(EntityRegistry.MAGIC_CIRCLE.get(), level);
        setPos(position);
        setRot(yRot, xRot);
        this.duration = duration;
        this.noPhysics = true;
        this.CLOCKWISE = clockwise;
        setAnimationSpeed(animationSpeed);
        setColor(color);
    }

    public void setColor(int color) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_COLOR, color);
        }
    }

    public int getColor() {
        return this.getEntityData().get(DATA_COLOR);
    }

    public void setAnimationSpeed(float animationSpeed) {
        if (!this.level().isClientSide) {
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
        if (!level().isClientSide()) {
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    private PlayState predicate(AnimationState event) {
        String animationName;
        if (CLOCKWISE) {
            animationName = "animation.magic_circle.clockwise";
        }
        else {
            animationName = "animation.magic_circle.counterclockwise";
        }
        event.getController().setAnimation(RawAnimation.begin().then(animationName, Animation.LoopType.LOOP));
        event.getController().setAnimationSpeed(getAnimationSpeed());
        return PlayState.CONTINUE;
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
}
