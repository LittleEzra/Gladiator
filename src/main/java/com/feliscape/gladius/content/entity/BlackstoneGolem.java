package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.content.entity.ai.boss.BossAttackPattern;
import com.feliscape.gladius.content.entity.ai.boss.CyclicalAttackPattern;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BlackstoneGolem extends Boss implements Enemy {
    protected BlackstoneGolem(EntityType<? extends BlackstoneGolem> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MAX_HEALTH, 80.0);
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public BossAttackPattern createPattern() {
        return new CyclicalAttackPattern<BlackstoneGolem>(4,
                List.of(),
                List.of()
                );
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }

    public int getAmbientSoundInterval() {
        return 120;
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
