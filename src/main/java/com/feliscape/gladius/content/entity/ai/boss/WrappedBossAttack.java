package com.feliscape.gladius.content.entity.ai.boss;

public class WrappedBossAttack {
    private final BossAttack<?> attack;
    private boolean isRunning;

    public WrappedBossAttack(BossAttack<?> attack) {
        this.attack = attack;
    }

    public void start(){
        if (!isRunning) {
            isRunning = true;
            attack.start();
        }
    }
    public void stop(){
        if (isRunning) {
            isRunning = false;
            attack.stop();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public BossAttack<?> attack() {
        return attack;
    }
}
