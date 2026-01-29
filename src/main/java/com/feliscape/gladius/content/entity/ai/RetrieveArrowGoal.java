package com.feliscape.gladius.content.entity.ai;

import com.feliscape.gladius.GladiusServerConfig;
import com.feliscape.gladius.content.entity.ArrowInterface;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class RetrieveArrowGoal extends Goal {
    private TamableAnimal mob;
    @Nullable
    private AbstractArrow targetedArrow;
    private int timeToRecalcPath;

    public RetrieveArrowGoal(TamableAnimal mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!GladiusServerConfig.CONFIG.wolvesRetrieveArrows.getAsBoolean()) return false;

        if (!mob.isAlive() || mob.getOwner() == null || !mob.getOwner().isAlive()) return false;
        if (mob.hasData(GladiusDataAttachments.PICKED_UP_ARROW)) return false;
        if (targetedArrow != null) return false;

        AbstractArrow arrow = findArrow();
        if (arrow == null){
            return false;
        } else{
            targetedArrow = arrow;
            return true;
        }
    }

    public AbstractArrow findArrow(){
        double distance = getFollowDistance();
        List<AbstractArrow> entities = mob.level()
                .getEntitiesOfClass(AbstractArrow.class, mob.getBoundingBox().inflate(distance),
                        a -> ((ArrowInterface)a).isInGround() && a.pickup == AbstractArrow.Pickup.ALLOWED && a.getOwner() == mob.getOwner());

        double bestDistance = -1.0;
        AbstractArrow closestArrow = null;

        for(AbstractArrow arrow : entities) {
            double d = arrow.distanceToSqr(mob.getX(), mob.getY(), mob.getZ());
            if (bestDistance == -1.0 || d < bestDistance) {
                bestDistance = d;
                closestArrow = arrow;
            }
        }
        return closestArrow;
    }

    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.getNavigation().isDone()) return false;
        if (mob.hasData(GladiusDataAttachments.PICKED_UP_ARROW)) return false;
        return targetedArrow != null && !targetedArrow.isRemoved() && targetedArrow.onGround();
    }

    @Override
    public void start() {
        if (targetedArrow == null) return;
        timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        if (targetedArrow != null){
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.mob.getNavigation().moveTo(targetedArrow, 1.0);
            }

            if (mob.distanceTo(targetedArrow) <= 2.0D){
                mob.setData(GladiusDataAttachments.PICKED_UP_ARROW.get(), targetedArrow.getPickupItemStackOrigin().copy());
                mob.take(targetedArrow, 1);
                targetedArrow.discard();

                mob.syncData(GladiusDataAttachments.PICKED_UP_ARROW);
                stop();
            }
        }
    }

    @Override
    public void stop() {
        targetedArrow = null;
        this.mob.getNavigation().stop();
    }
}
