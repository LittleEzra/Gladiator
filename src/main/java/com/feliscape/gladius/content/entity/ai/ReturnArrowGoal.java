package com.feliscape.gladius.content.entity.ai;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;

public class ReturnArrowGoal extends Goal {
    private TamableAnimal mob;
    private final PathNavigation navigation;
    private int timeToRecalcPath;

    public ReturnArrowGoal(TamableAnimal mob) {
        this.mob = mob;
        navigation = mob.getNavigation();
    }

    @Override
    public boolean canUse() {
        var owner = mob.getOwner();
        if (owner == null){
            return false;
        } else if (mob.unableToMoveToOwner()){
            return false;
        }
        return mob.hasData(GladiusDataAttachments.PICKED_UP_ARROW) && !mob.getData(GladiusDataAttachments.PICKED_UP_ARROW).isEmpty();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.navigation.stop();
    }

    @Override
    public void tick() {
        var owner = mob.getOwner();
        if (owner == null) return;

        this.mob.getLookControl().setLookAt(owner, 10.0F, (float)this.mob.getMaxHeadXRot());

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.navigation.moveTo(owner, 1.0);
        }

        if (mob.distanceTo(owner) <= 2.0D){
            ItemStack stack = mob.getData(GladiusDataAttachments.PICKED_UP_ARROW);
            if (!(owner instanceof Player player) || !player.getInventory().add(stack)){
                ItemEntity item = new ItemEntity(mob.level(), mob.getX(), mob.getY(0.5D), mob.getZ(), stack);
                mob.level().addFreshEntity(item);
            }
            mob.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ITEM_PICKUP, owner.getSoundSource(), 0.5F, 1.0F);
            mob.removeData(GladiusDataAttachments.PICKED_UP_ARROW);
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else if (!this.mob.hasData(GladiusDataAttachments.PICKED_UP_ARROW)){
            return false;
        } else {
            return !this.mob.unableToMoveToOwner();
        }
    }
}
