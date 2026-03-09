package com.feliscape.gladius.content.entity.team;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class Alliance {
    public boolean isPartOf(Entity entity){
        return entity.hasData(GladiusDataAttachments.ALLIANCE) && entity.getData(GladiusDataAttachments.ALLIANCE) == this;
    }

    public abstract int getColor(Entity entity);
}
