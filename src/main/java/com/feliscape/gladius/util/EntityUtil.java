package com.feliscape.gladius.util;

import net.minecraft.world.entity.LivingEntity;

public class EntityUtil {
    public static float getHealthPercentage(LivingEntity living){
        return living.getHealth() / living.getMaxHealth();
    }
}
