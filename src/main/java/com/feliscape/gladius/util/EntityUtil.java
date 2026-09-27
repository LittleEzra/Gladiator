package com.feliscape.gladius.util;

import com.feliscape.gladius.content.attachment.AllianceData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityUtil {
    public static float getHealthPercentage(LivingEntity living){
        return living.getHealth() / living.getMaxHealth();
    }

    public static boolean areAllied(LivingEntity a, LivingEntity b){
        if (a.hasData(AllianceData.type())){
            return a.getData(AllianceData.type()).isPartOf(b);
        }
        return a.isAlliedTo(b);
    }

    public static AABB growToInclude(AABB aabb, Vec3 point){
        if (aabb.contains(point)) return aabb;
        double minX = aabb.minX;
        double minY = aabb.minY;
        double minZ = aabb.minZ;
        double maxX = aabb.maxX;
        double maxY = aabb.maxY;
        double maxZ = aabb.maxZ;

        minX = Math.min(minX, point.x);
        minY = Math.min(minY, point.y);
        minZ = Math.min(minZ, point.z);
        maxX = Math.max(maxX, point.x);
        maxY = Math.max(maxY, point.y);
        maxZ = Math.max(maxZ, point.z);

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
