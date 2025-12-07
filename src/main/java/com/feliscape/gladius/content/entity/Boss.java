package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.content.entity.ai.boss.BossAttack;
import com.feliscape.gladius.content.entity.ai.boss.BossAttackPattern;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class Boss extends PathfinderMob {
    @Nullable
    protected BossAttackPattern pattern;

    protected Boss(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.pattern = createPattern();
    }

    public abstract BossAttackPattern createPattern();
}
