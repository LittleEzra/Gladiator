package com.feliscape.gladius.content.entity.team;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.world.entity.Entity;

public class EmptyAlliance extends Alliance{

    @Override
    public boolean isPartOf(Entity entity) {
        return !entity.hasData(GladiusDataAttachments.ALLIANCE);
    }

    @Override
    public int getColor(Entity entity) {
        return 0xffffff;
    }
}
