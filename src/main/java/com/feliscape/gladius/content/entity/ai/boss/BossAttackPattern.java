package com.feliscape.gladius.content.entity.ai.boss;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BossAttackPattern {
    protected static final WrappedGoal NO_GOAL = new WrappedGoal(Integer.MAX_VALUE, new Goal() {
        @Override
        public boolean canUse() {
            return false;
        }
    }) {
        @Override
        public boolean isRunning() {
            return false;
        }
    };

    private final Set<WrappedGoal> availableGoals = new ObjectLinkedOpenHashSet<>();
    private final Supplier<ProfilerFiller> profiler;
    private final EnumSet<Goal.Flag> disabledFlags = EnumSet.noneOf(Goal.Flag.class);

    public BossAttackPattern(Supplier<ProfilerFiller> profiler) {
        this.profiler = profiler;
    }

    public void addGoal(int priority, Goal goal) {
        this.availableGoals.add(new WrappedGoal(priority, goal));
    }

    @VisibleForTesting
    public void removeAllGoals(Predicate<Goal> filter) {
        this.availableGoals.removeIf(p_262564_ -> filter.test(p_262564_.getGoal()));
    }

    public void removeGoal(Goal goal) {
        for (WrappedGoal wrappedgoal : this.availableGoals) {
            if (wrappedgoal.getGoal() == goal && wrappedgoal.isRunning()) {
                wrappedgoal.stop();
            }
        }

        this.availableGoals.removeIf(p_25378_ -> p_25378_.getGoal() == goal);
    }

    private static boolean goalContainsAnyFlags(WrappedGoal goal, EnumSet<Goal.Flag> flag) {
        for (Goal.Flag goal$flag : goal.getFlags()) {
            if (flag.contains(goal$flag)) {
                return true;
            }
        }

        return false;
    }

    public void tick() {
        ProfilerFiller profilerfiller = this.profiler.get();
        profilerfiller.push("bossAttackPatternCleanup");

        boolean isGoalRunning = false;

        for (WrappedGoal wrappedGoal : this.availableGoals) {
            if (wrappedGoal.isRunning()){
                if (goalContainsAnyFlags(wrappedGoal, this.disabledFlags) || !wrappedGoal.canContinueToUse()) {
                    wrappedGoal.stop();
                } else{
                    isGoalRunning = true;
                }
            }
        }

        profilerfiller.pop();

        if (!isGoalRunning) {

            profilerfiller.push("bossAttackPatternUpdate");

            var goal = getValidGoal();
            if (goal != null){
                goal.start();
            }

            profilerfiller.pop();
        }
        this.tickRunningGoals(true);
    }

    @Nullable
    public WrappedGoal getValidGoal(){
        for (WrappedGoal wrappedGoal : this.availableGoals) {
            if (!wrappedGoal.isRunning()
                    && !goalContainsAnyFlags(wrappedGoal, this.disabledFlags)
                    && wrappedGoal.canUse()) {
                return wrappedGoal;
            }
        }
        return null;
    }

    public void tickRunningGoals(boolean tickAllRunning) {
        ProfilerFiller profilerfiller = this.profiler.get();
        profilerfiller.push("goalTick");

        for (WrappedGoal wrappedgoal : this.availableGoals) {
            if (wrappedgoal.isRunning() && (tickAllRunning || wrappedgoal.requiresUpdateEveryTick())) {
                wrappedgoal.tick();
            }
        }

        profilerfiller.pop();
    }

    public Set<WrappedGoal> getAvailableGoals() {
        return this.availableGoals;
    }

    public void disableControlFlag(Goal.Flag flag) {
        this.disabledFlags.add(flag);
    }

    public void enableControlFlag(Goal.Flag flag) {
        this.disabledFlags.remove(flag);
    }

    public void setControlFlag(Goal.Flag flag, boolean enabled) {
        if (enabled) {
            this.enableControlFlag(flag);
        } else {
            this.disableControlFlag(flag);
        }
    }
}
