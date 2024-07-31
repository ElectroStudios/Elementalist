package net.electro.elementalist.item.bracelets;

import net.electro.elementalist.util.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ResourceBundle;

public class ChargedStaff extends Item {
    public final Element ELEMENT;
    public ChargedStaff(Properties pProperties) {
        super(pProperties);
        ELEMENT = null;
    }

    public ChargedStaff(Properties properties, Element element) {
        super(properties);
        this.ELEMENT = element;
    }

    public void addNbtData(int spellSlotId, ResourceLocation spellId, ItemStack itemStack) {
        CompoundTag nbtData;
        if (itemStack.hasTag()) {
            nbtData = itemStack.getTag();
        }
        else
        {
            nbtData = new CompoundTag();
        }
        nbtData.putString("elementalist.spell_slot_" + spellSlotId, spellId.toString());
        itemStack.setTag(nbtData);
    }

    public ResourceLocation getNbtData(int spellSlotId, ItemStack itemStack) {
        return new ResourceLocation(itemStack.getTag().getString("elementalist.spell_slot_" + spellSlotId));
    }

    public boolean hasNbtData(int spellSlotId, ItemStack itemStack) {
        if (itemStack.getTag() == null) {
            return false;
        }
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
