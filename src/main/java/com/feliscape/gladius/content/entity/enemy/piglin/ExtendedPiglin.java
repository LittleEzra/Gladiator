package com.feliscape.gladius.content.entity.enemy.piglin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.level.Level;

public abstract class ExtendedPiglin extends AbstractPiglin {
    public ExtendedPiglin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    public ExtendedPiglinArmPose getExtendedPose(){
        return ExtendedPiglinArmPose.NONE;
    }
}
