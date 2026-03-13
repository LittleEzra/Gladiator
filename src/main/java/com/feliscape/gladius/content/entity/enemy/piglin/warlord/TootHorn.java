package com.feliscape.gladius.content.entity.enemy.piglin.warlord;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.ThrownBomb;
import com.feliscape.gladius.content.item.projectile.BombItem;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public class TootHorn<E extends Mob> extends Behavior<E> {
    private static final int TIMEOUT = 80;

    public TootHorn() {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                GladiusMemoryModuleTypes.TOOT_HORN_DELAY.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), TIMEOUT);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        LivingEntity livingentity = getAttackTarget(owner);
        if (livingentity == null) return false;
        return owner.getItemBySlot(EquipmentSlot.OFFHAND).has(DataComponents.INSTRUMENT) &&
                BehaviorUtils.canSee(owner, livingentity) && livingentity.closerThan(owner, 8);
    }

    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return entity.isUsingItem() && this.checkExtraStartConditions(level, entity);
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {

        ItemStack itemstack = entity.getOffhandItem();
        Holder<Instrument> instrument = itemstack.get(DataComponents.INSTRUMENT);
        if (instrument != null) {
            Gladius.LOGGER.debug("Tooting horn");
            entity.startUsingItem(InteractionHand.OFF_HAND);
            SoundEvent soundevent = instrument.value().soundEvent().value();
            float volume = instrument.value().range() / 16.0F;
            level.playSound(null, entity, soundevent, SoundSource.RECORDS, volume, 1.0F);
            level.gameEvent(GameEvent.INSTRUMENT_PLAY, entity.position(), GameEvent.Context.of(entity));
        }

        entity.getBrain().setMemory(GladiusMemoryModuleTypes.TOOT_HORN_DELAY.get(), 400 - level.getDifficulty().getId() * 40);
    }

    protected void tick(ServerLevel level, E owner, long gameTime) {
        if (!owner.isUsingItem()) {
            owner.startUsingItem(InteractionHand.OFF_HAND);
        }

        if (owner.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)){
            owner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }

    protected void stop(ServerLevel level, E entity, long gameTime) {
        if (entity.isUsingItem()) {
            entity.stopUsingItem();
        }
    }

    private void lookAtTarget(Mob shooter, LivingEntity target) {
        shooter.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity shooter) {
        return shooter.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }
}
