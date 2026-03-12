package com.feliscape.gladius.content.world.invasion;

import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolem;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Supplier;

public class PiglinInvasion {
    private static final Component RAID_NAME_COMPONENT = Component.translatable("event.gladius.piglin_invasion");

    private final Map<Integer, Set<AbstractPiglin>> groupInvaderMap = Maps.newHashMap();

    private BlockPos center;
    private List<BlockPos> portals = new ArrayList<>();
    private final int id;
    private final ServerLevel level;
    private final ServerBossEvent invasionEvent = new ServerBossEvent(RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
    private final int numGroups;

    private boolean started;
    private long ticksActive;
    private InvasionStatus status;
    private boolean active;
    private int groupsSpawned;

    public PiglinInvasion(int id, ServerLevel level, BlockPos center) {
        this.id = id;
        this.level = level;
        this.center = center;
        this.numGroups = this.getNumGroups(level.getDifficulty());
    }

    public int getNumGroups(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 2;
            case NORMAL -> 3;
            case HARD -> 5;
            default -> 0;
        };
    }

    public void tick(){
        if (this.isStopped()) return;

        if (this.status == InvasionStatus.ONGOING){
            boolean wasActive = this.active;
            this.active = this.level.hasChunkAt(center);
            if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
                this.stop();
                return;
            }

            if (wasActive != this.active){
                this.invasionEvent.setVisible(this.active);
            }

            if (!this.active) return;

            this.ticksActive++;
            if (this.ticksActive >= 48000L) {
                this.stop();
                return;
            }

            int tries = 0;

            while (shouldSpawnGroup()){
                BlockPos randomPortal = this.findNetherPortal(48);
                if (randomPortal != null){
                    this.started = true;
                    this.spawnGroup(randomPortal);
                } else{
                    tries++;
                }

                if (tries > 3) {
                    this.stop();
                    break;
                }
            }
        }
    }

    private void spawnGroup(BlockPos pos) {
        int wave = this.groupsSpawned + 1;
        DifficultyInstance difficulty = this.level.getCurrentDifficultyAt(pos);

        for (InvaderType invaderType : InvaderType.VALUES){
            int numSpawns = this.getDefaultNumSpawns(invaderType, wave, false);
            for (int i = 0; i < numSpawns; i++){
                var invader = invaderType.entityTypeSupplier.get().create(this.level);
                if (invader == null) break;

                invader.addEffect(new MobEffectInstance(GladiusMobEffects.STABILITY, 12 * 60 * 20));

                this.level.addFreshEntity(invader);
            }
        }

        if (wave == numGroups - 1){
            BlackstoneGolem golem = GladiusEntityTypes.BLACKSTONE_GOLEM.get().create(this.level);
            if (golem != null)
                this.level.addFreshEntity(golem);
        }
    }
    private int getDefaultNumSpawns(InvaderType invaderType, int wave, boolean shouldSpawnBonusGroup) {
        return invaderType.spawnsPerWaveBeforeBonus[wave];
    }

    private boolean shouldSpawnGroup() {
        return(this.groupsSpawned < this.numGroups) && this.getTotalInvadersAlive() == 0;
    }

    public int getTotalInvadersAlive() {
        return this.groupInvaderMap.values().stream().mapToInt(Set::size).sum();
    }

    public void stop() {
        this.active = false;
        this.invasionEvent.removeAllPlayers();
        this.status = InvasionStatus.STOPPED;
    }

    public boolean isOver() {
        return this.isVictory() || this.isLoss();
    }

    public boolean isStopped() {
        return this.status == InvasionStatus.STOPPED;
    }

    public boolean isVictory() {
        return this.status == InvasionStatus.VICTORY;
    }

    public boolean isLoss() {
        return this.status == InvasionStatus.LOSS;
    }

    private BlockPos findNetherPortal(int distance){
        List<BlockPos> allPortals = level.getPoiManager().findAll(
                poiTypeHolder -> poiTypeHolder.is(PoiTypes.NETHER_PORTAL),
                pos -> pos.distToCenterSqr(center.getCenter()) < distance,
                center,
                distance,
                PoiManager.Occupancy.ANY)
                .filter(this::validateNetherPortal).toList();
        if (allPortals.isEmpty()){
            return null;
        }
        return allPortals.get(level.random.nextInt(allPortals.size()));
    }

    private boolean validateNetherPortal(BlockPos pos){
        BlockPos below = pos.below();
        return level.getBlockState(below).isCollisionShapeFullBlock(level, below);
    }

    public Level getLevel() {
        return this.level;
    }

    public enum InvasionStatus{
        ONGOING,
        VICTORY,
        LOSS,
        STOPPED;

        private static final PiglinInvasion.InvasionStatus[] VALUES = values();

        static PiglinInvasion.InvasionStatus getByName(String name) {
            for (PiglinInvasion.InvasionStatus status : VALUES) {
                if (name.equalsIgnoreCase(status.name())) {
                    return status;
                }
            }

            return ONGOING;
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    public enum InvaderType{
        PIGLIN(() -> EntityType.PIGLIN, 0, 4, 3, 3, 4, 4, 4, 2),
        BRUTE(() -> EntityType.PIGLIN_BRUTE, 0, 0, 1, 2, 3, 2, 2, 3),
        SHAMAN(GladiusEntityTypes.PIGLIN_SHAMAN::get, 0, 0, 1, 0, 2, 2, 0, 2),
        BOMBER(GladiusEntityTypes.PIGLIN_BOMBER::get, 0, 0, 0, 1, 0, 1, 1, 0);

        static final InvaderType[] VALUES = values();

        final Supplier<EntityType<? extends AbstractPiglin>> entityTypeSupplier;
        final int[] spawnsPerWaveBeforeBonus;

        InvaderType(Supplier<EntityType<? extends AbstractPiglin>> entityTypeSupplier, int... spawnsPerWaveBeforeBonus) {
            this.entityTypeSupplier = entityTypeSupplier;
            this.spawnsPerWaveBeforeBonus = spawnsPerWaveBeforeBonus;
        }
    }
}
