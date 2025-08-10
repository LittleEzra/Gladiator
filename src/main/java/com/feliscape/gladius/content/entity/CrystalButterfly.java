package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class CrystalButterfly extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> DATA_IDLE = SynchedEntityData.defineId(CrystalButterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HAS_TARGET = SynchedEntityData.defineId(CrystalButterfly.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private UUID searchTargetUUID = null;
    @Nullable
    private Entity cachedSearchTarget = null;
    private boolean foundIdlePosition;

    public CrystalButterfly(EntityType<CrystalButterfly> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public CrystalButterfly(Level level, double x, double y, double z, @Nullable Component targetName) {
        this(GladiusEntityTypes.CRYSTAL_BUTTERFLY.get(), level);
        this.setPos(x, y, z);
        this.setCustomName(targetName);
        if (!level.isClientSide() && targetName != null) {
            var players = level.players();
            for (Player player : players) {
                if (player.getName().getString().equals(targetName.getString())) {
                    setSearchTarget(player);
                    break;
                }
            }
            setClientHasTarget(cachedSearchTarget == null);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.GRAVITY, 0.08)
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.FLYING_SPEED, 0.15F)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new FollowSearchTargetGoal(this));
        this.goalSelector.addGoal(4, new FindIdlePosition(this));
        this.goalSelector.addGoal(7, new WanderGoal(this, 1.0D));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isIdle()) {
            this.applyGravity();
        }
    }

    @Override
    public boolean isNoGravity() {
        return super.isNoGravity() && !this.isIdle();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {

            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                this.moveRelative(this.getSpeed(), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(this.onGround() ? 0.5F : 0.91F));
            }

        }

        this.calculateEntityAnimation(false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos p_27947_) {
                return !this.level.getBlockState(p_27947_.below()).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected double getDefaultGravity() {
        return this.isIdle() ? super.getDefaultGravity() : 0.0D;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IDLE, false);
        builder.define(DATA_HAS_TARGET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (searchTargetUUID != null) {
            compound.putUUID("SearchTarget", this.searchTargetUUID);
        }
        compound.putBoolean("Idle", this.isIdle());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("SearchTarget")) {
            this.searchTargetUUID = compound.getUUID("SearchTarget");
            this.setClientHasTarget(true);
        }
        this.setIdle(compound.getBoolean("Idle"));
    }

    public boolean isIdle() {
        return this.entityData.get(DATA_IDLE);
    }

    public void setIdle(boolean idle) {
        this.entityData.set(DATA_IDLE, idle);
    }

    public boolean hasSearchTarget() {
        if (level().isClientSide()) return this.entityData.get(DATA_HAS_TARGET);

        var target = getSearchTarget();
        return target != null;
    }

    public void setClientHasTarget(boolean hasTarget) {
        this.entityData.set(DATA_HAS_TARGET, hasTarget);
    }

    public void removeTarget() {
        this.searchTargetUUID = null;
        this.cachedSearchTarget = null;
        this.setClientHasTarget(false);
    }

    public void setSearchTarget(@Nullable Entity owner) {
        if (owner != null) {
            this.searchTargetUUID = owner.getUUID();
            this.cachedSearchTarget = owner;
        }
    }

    @Nullable
    public Entity getSearchTarget() {
        if (this.cachedSearchTarget != null && !this.cachedSearchTarget.isRemoved()) {
            return this.cachedSearchTarget;
        } else if (this.searchTargetUUID != null && this.level() instanceof ServerLevel serverlevel) {
            this.cachedSearchTarget = serverlevel.getEntity(this.searchTargetUUID);
            return this.cachedSearchTarget;
        } else {
            return null;
        }
    }

    public ItemStack asItem() {
        ItemStack itemStack = new ItemStack(GladiusItems.CRYSTAL_BUTTERFLY.get());
        itemStack.set(DataComponents.CUSTOM_NAME, getCustomName());
        return itemStack;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = this.asItem();
        if (!player.getInventory().add(itemStack)) {
            player.drop(itemStack, false);
        }
        this.discard();

        return InteractionResult.SUCCESS;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        float f = pos.getY() > this.getY() ? 2.0F : 1.0F;
        if (level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above())) {
            return 10.0F * f;
        }
        return 0.0F;
    }

    static class FollowSearchTargetGoal extends Goal {
        private final CrystalButterfly butterfly;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int circlingTicks = 0;

        FollowSearchTargetGoal(CrystalButterfly butterfly) {
            this.butterfly = butterfly;
        }

        @Override
        public boolean canUse() {
            Entity target = this.butterfly.getSearchTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                Path path = this.butterfly.getNavigation().createPath(target, 1);
                return path != null || this.butterfly.distanceTo(target) > 2.0D;
            }
        }

        @Override
        public boolean canContinueToUse() {
            Entity target = this.butterfly.getSearchTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                return this.butterfly.isWithinRestriction(target.blockPosition());
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            Entity target = this.butterfly.getSearchTarget();
            if (target != null) {
                this.butterfly.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (this.butterfly.distanceTo(target) < 2.5D) {
                    circlingTicks++;
                    double theta = circlingTicks * 0.5D * this.butterfly.getSpeed();
                    this.pathedTargetX = target.getX() + Math.cos(theta) * 1.5D;
                    this.pathedTargetY = target.getY(0.75D);
                    this.pathedTargetZ = target.getZ() + Math.sin(theta) * 1.5D;
                    this.butterfly.getNavigation().moveTo(pathedTargetX, pathedTargetY, pathedTargetZ, 0, 1.0D);
                } else if ((this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                        || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                        || this.butterfly.getRandom().nextFloat() < 0.05F)) {
                    circlingTicks = 0;

                    this.pathedTargetX = target.getX();
                    this.pathedTargetY = target.getY(0.75D);
                    this.pathedTargetZ = target.getZ();
                    this.butterfly.getNavigation().moveTo(pathedTargetX, pathedTargetY, pathedTargetZ, 0, 1.0D);
                }
            }
        }

        @Override
        public void stop() {
            this.butterfly.getNavigation().stop();
        }
    }

    static class FindIdlePosition extends Goal {
        private final CrystalButterfly butterfly;
        private BlockPos blockPos = null;

        FindIdlePosition(CrystalButterfly butterfly) {
            this.butterfly = butterfly;
        }

        @Override
        public boolean canUse() {
            if (this.butterfly.isIdle() || this.butterfly.hasSearchTarget()) return false;

            Entity target = this.butterfly.getSearchTarget();
            if (target != null) {
                return !target.isAlive();
            } else {
                this.blockPos = this.findRestingSpot();
                if (this.blockPos == null) {
                    return false;
                }
                Path path = this.butterfly.getNavigation().createPath(blockPos, 1);
                if (path != null) this.butterfly.foundIdlePosition = true;
                return path != null;
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.butterfly.isIdle() || this.butterfly.hasSearchTarget()) return false;

            Entity target = this.butterfly.getSearchTarget();
            if (target != null) {
                return !target.isAlive();
            } else {
                return blockPos != null && this.butterfly.isWithinRestriction(blockPos);
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        private BlockPos findRestingSpot() {
            /*Level level = this.butterfly.level();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int y = -4; y <= 4; y++){
                for (int x = -8; x <= 8; x++){
                    for (int z = -8; z <= 8; z++){
                        mutable.setWithOffset(this.butterfly.blockPosition(), x, y, z);
                        if (!level.isEmptyBlock(mutable) && level.isEmptyBlock(mutable.above())){
                            return mutable;
                        }
                    }
                }
            }*/
            var randomPos = LandRandomPos.getPos(this.butterfly, 8, 4);
            return randomPos == null ? null : BlockPos.containing(randomPos);
        }

        @Override
        public void tick() {
            if (blockPos == null) return;

            double pathedTargetX = blockPos.getX() + 0.5D;
            double pathedTargetY = blockPos.getY() + 1.0D;
            double pathedTargetZ = blockPos.getZ() + 0.5D;

            this.butterfly.getLookControl().setLookAt(pathedTargetX, pathedTargetY, pathedTargetZ, 30.0F, 30.0F);
            if ((this.blockPos.getX() == 0 && this.blockPos.getY() == 0 && this.blockPos.getZ() == 0
                    || this.butterfly.getRandom().nextFloat() < 0.05F)) {
                this.butterfly.getNavigation().moveTo(pathedTargetX, pathedTargetY, pathedTargetZ, 1.0D);
            }

            if (this.butterfly.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) < Mth.square(2.0D)) {
                this.butterfly.setIdle(true);
                this.stop();
            }
        }

        @Override
        public void stop() {
            this.butterfly.getNavigation().stop();
        }
    }


    static class WanderGoal extends WaterAvoidingRandomStrollGoal {
        private final CrystalButterfly butterfly;

        public WanderGoal(CrystalButterfly butterfly, double speedModifier) {
            super(butterfly, speedModifier, 1.0F);
            this.butterfly = butterfly;
        }

        @Override
        public boolean canUse() {
            return !butterfly.isIdle() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !butterfly.isIdle() && super.canContinueToUse();
        }
        @Nullable
        @Override
        protected Vec3 getPosition() {
            Vec3 viewVector = this.mob.getViewVector(0.0F);
            Vec3 randomPos = HoverRandomPos.getPos(this.mob, 8, 3, viewVector.x, viewVector.z, (float) (Math.PI / 2), 3, 1);
            return randomPos != null ? randomPos : AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, viewVector.x, viewVector.z, (float) (Math.PI / 2));
        }
    }
}
