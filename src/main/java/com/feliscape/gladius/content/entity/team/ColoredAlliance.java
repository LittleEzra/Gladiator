package com.feliscape.gladius.content.entity.team;

import net.minecraft.world.entity.Entity;

public class ColoredAlliance extends Alliance{
    private int color;

    public ColoredAlliance(int color) {
        this.color = color;
    }

    @Override
    public int getColor(Entity entity) {
        return color;
    }
}
