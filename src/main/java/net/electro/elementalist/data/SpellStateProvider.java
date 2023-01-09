package net.electro.elementalist.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellStateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<SpellState> SPELL_STATE = CapabilityManager.get(new CapabilityToken<SpellState>() { });

    private SpellState spellState = null;
    private final LazyOptional<SpellState> optional = LazyOptional.of(this::createSpellState);

    private SpellState createSpellState() {
        if (this.spellState == null)
        {
            this.spellState = new SpellState();
        }

        return this.spellState;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SPELL_STATE)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSpellState().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSpellState().loadNBTData(nbt);
    }
}
