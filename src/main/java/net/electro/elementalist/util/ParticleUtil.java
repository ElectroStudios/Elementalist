package net.electro.elementalist.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class ParticleUtil {
    public static void createParticleCircle(ParticleOptions particleOptions, Level level, float speed, Vec3 position, float yaw, float pitch, float startCircleRadius) {
        for (int i = 0; i < 360; i += 2) {
            double x = Math.cos(Math.toRadians(i));
            double y = Math.sin(Math.toRadians(i));
            Vec3 speedVec = new Vec3(x, y, 0);
            speedVec = speedVec.xRot((float)Math.toRadians(-pitch));
            speedVec = speedVec.yRot((float)Math.toRadians(-yaw));
            level.addParticle(particleOptions, position.x + speedVec.x * startCircleRadius,
                    position.y + speedVec.y * startCircleRadius, position.z + speedVec.z * startCircleRadius,
                    speedVec.x * speed, speedVec.y * speed, speedVec.z * speed);
        }
    }
}
