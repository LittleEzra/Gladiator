package com.feliscape.gladius.content.entity.ai.boss;

import com.feliscape.gladius.content.entity.Boss;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class CyclicalAttackPattern<T extends Boss> extends BossAttackPattern{
    private ArrayList<WrappedBossAttack> transitionAttacks;
    private ArrayList<WrappedBossAttack> finishAttacks;
    private int cycleDuration;
    int positionInCycle;

    public CyclicalAttackPattern(int cycleDuration, List<BossAttack<T>> transitionAttacks, List<BossAttack<T>> finishAttacks) {
        this.cycleDuration = cycleDuration;
        this.transitionAttacks = new ArrayList<>(transitionAttacks.stream().map(attack -> new WrappedBossAttack(attack)).toList());
        this.finishAttacks = new ArrayList<>(finishAttacks.stream().map(attack -> new WrappedBossAttack(attack)).toList());
        for (var attack : transitionAttacks){
            this.registerAttack(attack);
        }
        for (var attack : finishAttacks){
            this.registerAttack(attack);
        }
    }

    @Override
    public WrappedBossAttack chooseAttack(Boss boss) {
        positionInCycle = (positionInCycle + 1) % cycleDuration;
        if (positionInCycle == 0){
            return finishAttacks.get(boss.getRandom().nextInt(finishAttacks.size()));
        }
        return transitionAttacks.get(boss.getRandom().nextInt(transitionAttacks.size()));
    }
}
