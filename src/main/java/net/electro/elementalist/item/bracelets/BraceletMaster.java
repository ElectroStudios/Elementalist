package net.electro.elementalist.item.bracelets;

import net.electro.elementalist.util.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.C;

public class BraceletMaster extends Item {
    public final Element ELEMENT;
    public BraceletMaster(Properties pProperties) {
        super(pProperties);
        ELEMENT = null;
    }

    public BraceletMaster(Properties properties, Element element) {
        super(properties);
        this.ELEMENT = element;
    }

    public void addNbtData(int spellSlotId, int spellId, ItemStack itemStack) {
        CompoundTag nbtData;
        if (itemStack.hasTag()) {
            nbtData = itemStack.getTag();
        }
        else
        {
            nbtData = new CompoundTag();
        }
        nbtData.putInt("elementalist.spell_slot_" + spellSlotId, spellId);
        itemStack.setTag(nbtData);
    }

    public int getNbtData(int spellSlotId, ItemStack itemStack) {
        return itemStack.getTag().getInt("elementalist.spell_slot_" + spellSlotId);
    }

    public boolean hasNbtData(int spellSlotId, ItemStack itemStack) {
        return itemStack.getTag().contains("elementalist.spell_slot_" + spellSlotId);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return true;
    }
}
