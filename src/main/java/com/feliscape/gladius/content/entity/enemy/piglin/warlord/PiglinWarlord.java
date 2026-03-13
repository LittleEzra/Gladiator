package com.feliscape.gladius.content.entity.enemy.piglin.warlord;

import com.feliscape.gladius.content.entity.enemy.piglin.ExtendedPiglin;
import com.feliscape.gladius.content.entity.enemy.piglin.ExtendedPiglinArmPose;
import com.feliscape.gladius.content.entity.enemy.piglin.bomber.PiglinBomberAi;
import com.feliscape.gladius.content.entity.enemy.piglin.shaman.PiglinShaman;
import com.feliscape.gladius.content.entity.enemy.piglin.shaman.PiglinShamanAi;
import com.feliscape.gladius.registry.GladiusItems;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PiglinWarlord extends ExtendedPiglin {
    public PiglinWarlord(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, (double)50.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F)
                .add(Attributes.ATTACK_DAMAGE, (double)1.0F);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        PiglinWarlordAi.initMemories(this);
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GladiusItems.GOLDEN_SCIMITAR.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(GladiusItems.HOGLIN_TUSK.get()));
        this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
    }

    @Override
    protected Brain.Provider<PiglinWarlord> brainProvider() {
        return Brain.provider(PiglinWarlordAi.MEMORY_TYPES, PiglinWarlordAi.SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return PiglinWarlordAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<PiglinWarlord> getBrain() {
        return (Brain<PiglinWarlord>)super.getBrain();
    }


    @Override
    protected boolean canHunt() {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("piglinBomberBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        PiglinWarlordAi.updateActivity(this);
        PiglinWarlordAi.maybePlayActivitySound(this);
        super.customServerAiStep();
    }

    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && source.getEntity() instanceof LivingEntity) {
                PiglinWarlordAi.wasHurtBy(this, (LivingEntity)source.getEntity());
            }

            return flag;
        }
    }

    @Override
    public ExtendedPiglinArmPose getExtendedPose() {
        if (this.isUsingItem()) return ExtendedPiglinArmPose.TOOT_HORN;
        return super.getExtendedPose();
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (isUsingItem()) return PiglinArmPose.DEFAULT;
        return this.isAggressive() && this.isHoldingMeleeWeapon() ? PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON : PiglinArmPose.DEFAULT;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PIGLIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void playConvertedSound() {
        this.makeSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }
}
