package com.feliscape.gladius.content.entity.enemy.cloudstriker;

import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolem;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class CloudStriker extends PathfinderMob {
    public static final EntityDataAccessor<Integer> DATA_CHARGE = SynchedEntityData.defineId(CloudStriker.class, EntityDataSerializers.INT);

    protected CloudStriker(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }
}
