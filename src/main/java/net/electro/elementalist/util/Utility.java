package net.electro.elementalist.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Utility {
    public static Vec3 vectorInterp(Vec3 vec1, Vec3 vec2, float amount) {
        return vec1.add(vec2.subtract(vec1).multiply(amount, amount, amount));
    }

    public static Vec3 getRandomVectorSphere(RandomSource random, float radius, boolean fill) {
        Vec3 direction = Vec3.directionFromRotation(random.nextFloat() * 180 - 90, random.nextFloat() * 360 - 180);
        if (fill) {
            return multiplyVec3ByFloat(direction, radius * random.nextFloat());
        }
        else {
            return multiplyVec3ByFloat(direction, radius);
        }
    }

    public static Vec3 getRandomVectorCube(RandomSource random, AABB aabb) {
        return new Vec3(Mth.lerp(random.nextFloat(), aabb.minX, aabb.maxX),
                Mth.lerp(random.nextFloat(), aabb.minY, aabb.maxY),
                Mth.lerp(random.nextFloat(), aabb.minZ, aabb.maxZ));
    }

    public static Vec3 multiplyVec3ByFloat(Vec3 vector, float amount) {
        return vector.multiply(amount, amount, amount);
    }

    public static double getDistanceFromPointToVector(Vec3 vectorNormal, Vec3 vectorOrigin, Vec3 point) {
        return point.subtract(vectorOrigin).cross(vectorNormal).length()/vectorNormal.length();
    }

    public static Vec3 getDirectionToVector(Vec3 vec1, Vec3 vec2) {
        return vec2.subtract(vec1);
    }
}
