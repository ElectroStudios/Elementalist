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

public class ElementalistStatsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ElementalistStats> ELEMENTALIST_STATS = CapabilityManager.get(new CapabilityToken<ElementalistStats>() { });

    private ElementalistStats elementalistStats = null;
    private final LazyOptional<ElementalistStats> optional = LazyOptional.of(this::createElementalistStats);

    private ElementalistStats createElementalistStats() {
        if (this.elementalistStats == null)
        {
            this.elementalistStats = new ElementalistStats();
        }

        return this.elementalistStats;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ELEMENTALIST_STATS)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createElementalistStats().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createElementalistStats().loadNBTData(nbt);
    }
}
