package net.electro.elementalist.block.entity;

import net.electro.elementalist.entity.spells.MagicCircleEntity;
import net.electro.elementalist.registry.ItemRegistry;
import net.electro.elementalist.item.bracelets.UnchargedStaff;
import net.electro.elementalist.util.ParticleUtil;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AltarBlockEntity extends BlockEntity {
    private MagicCircleEntity magicCircle;
    public AltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ALTAR.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AltarBlockEntity entity) {

        ItemEntity staffEntity;
        BlockPos staffSearchPosition = blockPos.above();
        AABB staffSearchVolume = new AABB(staffSearchPosition.getX(), staffSearchPosition.getY(), staffSearchPosition.getZ(),
                staffSearchPosition.getX()+1f, staffSearchPosition.getY()+1f, staffSearchPosition.getZ()+1f);
        List<ItemEntity> itemEntities =  level.getEntitiesOfClass(ItemEntity.class,
                staffSearchVolume, itemEntity -> itemEntity.getItem().getItem() instanceof UnchargedStaff);
        if (!itemEntities.isEmpty()) {
            staffEntity = itemEntities.get(0);
            if (!level.isClientSide() && entity.magicCircle == null) {
                    Vec3 circlePos = new Vec3(blockPos.getX()+0.5f, blockPos.getY()-0.5f, blockPos.getZ()+0.5f);
                    entity.magicCircle = new MagicCircleEntity(circlePos, 0f, 90f, level, 1000, 0.2f, true, 0xFFFFFFFF) {
                        @Override
                        public void tick() {
                            if (level.isClientSide()) {
                                if (this.duration == 1000) {
                                    ParticleUtil.createParticleCircle(ParticleTypes.END_ROD, level, 0.5f, circlePos, 0f, 90f, 0f);
                                }
                            }
                            super.tick();
                        }
                    };
                level.addFreshEntity(entity.magicCircle);
                };
        }
        else {
            staffEntity = null;
            if (entity.magicCircle != null) {
                entity.magicCircle.discard();
                entity.magicCircle = null;
            }
        }

        if (level.isClientSide()) {
            if (staffEntity != null) {
                Vec3 randomSpeed = Utility.getRandomVectorSphere(level.getRandom(), 0.3f, false);
                level.addParticle(ParticleTypes.FLAME, true, blockPos.getX()+0.5f, blockPos.getY() + 1.2f,
                        blockPos.getZ()+0.5f, randomSpeed.x, randomSpeed.y, randomSpeed.z);
            }
        }
        BlockPos itemSearchPosition = blockPos.north().west();
        AABB itemSearchVolume = new AABB(itemSearchPosition.getX(), itemSearchPosition.getY(), itemSearchPosition.getZ(),
                itemSearchPosition.getX()+3f, itemSearchPosition.getY()+3f, itemSearchPosition.getZ()+3f);
        itemEntities =  level.getEntitiesOfClass(ItemEntity.class,
                itemSearchVolume, itemEntity -> true);
        if (!itemEntities.isEmpty()) {
            for (ItemEntity itemEntity : itemEntities) {
                Item item = itemEntity.getItem().getItem();
                if (item.equals(net.minecraft.world.item.Items.COAL)) {
                    staffEntity.setItem(new ItemStack(ItemRegistry.FIRE_BRACELET_IRON.get()));
                    itemEntity.discard();
                    break;
                }
                else if (item.equals(net.minecraft.world.item.Items.POTION) && PotionUtils.getPotion(itemEntity.getItem()).equals(Potions.WATER)) {
                    staffEntity.setItem(new ItemStack(ItemRegistry.WATER_BRACELET_IRON.get()));
                    itemEntity.discard();
                    break;
                }
                else if (item.equals(net.minecraft.world.item.Items.SNOW_BLOCK)) {
                    staffEntity.setItem(new ItemStack(ItemRegistry.ICE_BRACELET_IRON.get()));
                    itemEntity.discard();
                    break;
                }
            }
        }

    }
}
