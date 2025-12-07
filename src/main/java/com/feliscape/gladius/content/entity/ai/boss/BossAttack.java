package com.feliscape.gladius.content.entity.ai.boss;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public abstract class BossAttack<T extends LivingEntity> {
    protected T boss;

    public BossAttack(T boss) {
        this.boss = boss;
    }

    public abstract boolean canUse();

    public boolean canContinueUsing(){
        return canUse();
    }

    public void start(){

    }
    public void stop(){

    }

    public void useAttack(){

    }

    public void tick() {

    }
}
