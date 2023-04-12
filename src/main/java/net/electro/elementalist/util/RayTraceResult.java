package net.electro.elementalist.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RayTraceResult {
    public final Vec3 HIT_LOCATION;
    public final List<Entity> HIT_ENTITIES;
    public final boolean HIT;
    public RayTraceResult(boolean hit, Vec3 hitLocation, List<Entity> hitEntities)
    {
        this.HIT_ENTITIES = hitEntities;
        this.HIT_LOCATION = hitLocation;
        this.HIT = hit;
    }

    @Override
    public String toString() {
        return HIT_LOCATION.toString() + " " + HIT_ENTITIES.size();
    }
}
