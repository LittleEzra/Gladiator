package com.feliscape.gladius.content.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;

public class PiglinPatrolSpawner implements CustomSpawner {
    private int nextTick;

    @Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!spawnEnemies){
            return 0;
        } else if (!level.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)) {
            return 0;
        } else{
            RandomSource random = level.random;
            this.nextTick--;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick = this.nextTick + 12000 + random.nextInt(1200);
                return 0;
            }
        }
    }
}
