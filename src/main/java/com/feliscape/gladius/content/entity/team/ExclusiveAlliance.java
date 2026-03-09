package com.feliscape.gladius.content.entity.team;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ExclusiveAlliance extends ColoredAlliance{
    private TagKey<EntityType<?>> tag;

    public ExclusiveAlliance(int color, TagKey<EntityType<?>> tag) {
        super(color);
        this.tag = tag;
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return entity.getType().is(tag) && super.isPartOf(entity);
    }
}
