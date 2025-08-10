package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements EquipmentUser, Leashable, Targeting {
    @Shadow @Final public GoalSelector goalSelector;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updateControlFlags", at = @At(value = "RETURN", ordinal = 0))
    protected void disableAI(CallbackInfo ci) {
        if (hasEffect(GladiusMobEffects.STUN)){
            this.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
            this.goalSelector.setControlFlag(Goal.Flag.JUMP, false);
            this.goalSelector.setControlFlag(Goal.Flag.LOOK, false);
        }
    }
}
