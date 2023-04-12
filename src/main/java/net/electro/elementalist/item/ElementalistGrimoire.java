package net.electro.elementalist.item;

import net.electro.elementalist.client.renderer.item.GrimoireRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
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

public class ElementalistGrimoire extends Item implements IAnimatable, ISyncable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean shouldAnimate = false;
    private boolean animationInProgress = false;
    private static final int ANIM_OPEN = 0;
    private static final int ANIM_STAY_OPEN = 1;

    public ElementalistGrimoire(Properties pProperties) {
        super(pProperties);
        GeckoLibNetwork.registerSyncable(this);
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            if (pIsSelected || pEntity instanceof Player && ((Player) pEntity).getOffhandItem() == pStack) {
//                final int id = GeckoLibUtil.guaranteeIDForStack(pStack, (ServerLevel) pLevel);
//                // Tell all nearby clients to trigger this JackInTheBoxItem
//                final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pEntity);
//                GeckoLibNetwork.syncAnimation(target, this, id, ANIM_STAY_OPEN);

//                final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
//                if (shouldAnimate == false) {
//
//                    GeckoLibNetwork.syncAnimation(target, this, id, ANIM_OPEN);
//                }
//                else {
//                    Animation animation = controller.getCurrentAnimation();
//                    if (animation == null || !animation.animationName.equals("grimoire_open_process")) {
//                    }
//                }
                shouldAnimate = true;
            }
            else {
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
//        if (!shouldAnimate) {
//            return PlayState.STOP;
//        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                30, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
        if (state == ANIM_OPEN) {
            // Always use GeckoLibUtil to get AnimationControllers when you don't have
            // access to an AnimationEvent
            Animation animation = controller.getCurrentAnimation();
            if (animation == null || !animation.animationName.equals("grimoire_open_process")) {
                controller.markNeedsReload();
                controller.setAnimation(new AnimationBuilder()
                        .addAnimation("grimoire_open_process", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            }
        }
        else if (state == ANIM_STAY_OPEN) {
            controller.setAnimation(new AnimationBuilder()
                    .addAnimation("grimoire_open_loop", ILoopType.EDefaultLoopTypes.LOOP));
        }
    }
}
