package net.electro.elementalist.item;

import net.electro.elementalist.client.gui.GrimoireScreen;
import net.electro.elementalist.client.renderer.item.GrimoireRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ElementalistGrimoire extends Item implements GeoItem {
    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean shouldAnimate = false;
    private boolean animationInProgress = false;
    private static final int ANIM_OPEN = 0;
    private static final int ANIM_STAY_OPEN = 1;

    public ElementalistGrimoire(Properties pProperties) {
        super(pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pLevel instanceof ServerLevel serverLevel) {
            if (pIsSelected || pEntity instanceof Player && ((Player) pEntity).getOffhandItem() == pStack) {
                shouldAnimate = true;
                triggerAnim(pEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "open");
            }
            else if (shouldAnimate) {
                triggerAnim(pEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "close");
                shouldAnimate = false;
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (player.level() instanceof ServerLevel serverLevel && shouldAnimate) {
                triggerAnim(player, GeoItem.getOrAssignId(item, serverLevel), "controller", "close");
                shouldAnimate = false;
            }
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final GrimoireRenderer renderer = new GrimoireRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState state) {
        state.getController()
                .triggerableAnim("open", RawAnimation.begin().then("grimoire_open", Animation.LoopType.HOLD_ON_LAST_FRAME))
                .triggerableAnim("close", RawAnimation.begin().then("grimoire_close", Animation.LoopType.PLAY_ONCE));
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
