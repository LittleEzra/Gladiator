package com.feliscape.gladius.content.entity.ai.boss;

import com.feliscape.gladius.content.entity.Boss;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BossAttackPattern {
    @Nullable
    protected WrappedBossAttack activeAttack = null;
    private Set<WrappedBossAttack> availableAttacks = new ObjectLinkedOpenHashSet<>();

    public void tick(Boss boss){
        // Cleanup
        for (WrappedBossAttack wrapped : this.availableAttacks){
            if (wrapped.isRunning() && !wrapped.attack().canContinueUsing()){
                wrapped.stop();
                if (wrapped == activeAttack){
                    activeAttack = null;
                }
            }
        }

        if (activeAttack == null){
            activeAttack = chooseAttack(boss);
        }
    }

    public void registerAttack(BossAttack<?> attack){
        availableAttacks.add(new WrappedBossAttack(attack));
    }

    public abstract WrappedBossAttack chooseAttack(Boss boss);
}
