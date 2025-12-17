package com.feliscape.gladius.content.entity.ai.boss;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

import java.util.Set;
import java.util.function.Supplier;

public class CyclicalAttackPattern extends BossAttackPattern{
    private final Set<WrappedGoal> transitionAttacks = new ObjectLinkedOpenHashSet<>();
    private final Set<WrappedGoal> cycleAttacks = new ObjectLinkedOpenHashSet<>();

    public CyclicalAttackPattern(Supplier<ProfilerFiller> profiler) {
        super(profiler);
    }


    public void addTransitionAttack(int priority, Goal goal) {
        
    }

    public void addCycleAttack(int priority, Goal goal) {

    }
}
