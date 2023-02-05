package net.electro.elementalist.item;

import net.electro.elementalist.client.renderer.item.GrimoireRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ElementalistGrimoire extends Item implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean shouldAnimate = false;
    private boolean animationInProgress = false;
    public ElementalistGrimoire(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            if (pIsSelected || pEntity instanceof Player && ((Player) pEntity).getOffhandItem() == pStack) {
                if (shouldAnimate == false) {
                    animationInProgress = true;
                }
                else {
                    animationInProgress = false;
                }
                shouldAnimate = true;
            } else {
                shouldAnimate = false;
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new GrimoireRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (animationInProgress) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("grimoire_open_process", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
//            if (event.getController().getAnimationState() == AnimationState.Stopped) {
//            }
            return PlayState.CONTINUE;
        }
        Animation currentAnimation = event.getController().getCurrentAnimation();
        if ((currentAnimation == null || currentAnimation.animationName != "grimoire_open_process") && shouldAnimate) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("grimoire_open_loop", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                10, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


}
