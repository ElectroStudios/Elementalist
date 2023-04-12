package net.electro.elementalist.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Utility {

    public static void performBlit(ResourceLocation texture, PoseStack pPoseStack, int pX, int pY,
                             float pUOffset, float pVOffset, int pWidth, int pHeight,
                             int pTextureWidth, int pTextureHeight, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f,
                color.getAlpha() / 255f);
        RenderSystem.setShaderTexture(0, texture);
        Screen.blit(pPoseStack, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }



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

    public static RayTraceResult rayTraceEntities(Level level, Entity entity, Vec3 start, Vec3 direction, float distance, float accuracy, Predicate<? super Entity> predicate) {
        List<Entity> hitEntities = new ArrayList<>();
        int rayTraceDistance = (int)(distance * accuracy);
        Vec3 firstHit = null;
        boolean hit = false;
        for (int i = 0; i < rayTraceDistance; i++) {
            Vec3 rayTracePos = start.add(multiplyVec3ByFloat(direction, (i / (float)rayTraceDistance) * distance));
            AABB rayTraceAABB = new AABB(rayTracePos, rayTracePos);
            List<Entity> foundEntities = level.getEntities(entity, rayTraceAABB, predicate);
            if (firstHit == null && !foundEntities.isEmpty())
            {
                firstHit = rayTracePos;
                hit = true;
            }
            for (Entity foundEntity : foundEntities) {
                if (!hitEntities.contains(foundEntity)) {
                    hitEntities.add(foundEntity);
                }
            }
        }
        return new RayTraceResult(hit, firstHit, hitEntities);
    }

    public static int getExperienceToNextLevel(int currentLevel) {
        return (int)(Math.pow(currentLevel+1, 1.5f) * 100);
    }
}
