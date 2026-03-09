package com.feliscape.gladius.util;

import com.feliscape.gladius.content.attachment.AllianceData;
import net.minecraft.world.entity.LivingEntity;

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
}
