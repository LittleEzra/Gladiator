package com.feliscape.gladius.content.item;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.IceCharge;
import com.feliscape.gladius.content.entity.projectile.TorridWisp;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.GladiusTags;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class TorridStandardItem extends ProjectileWeaponItem {
    public TorridStandardItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return itemStack -> itemStack.is(GladiusTags.Items.TORRID_STANDARD_AMMO);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float v, float v1, float v2, @Nullable LivingEntity livingEntity1) {

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 20 == 0){
            if (!level.isClientSide()) {
                ItemStack ammoStack = ItemStack.EMPTY;
                if (livingEntity instanceof Player player){
                    ammoStack = player.getProjectile(stack);
                    if (ammoStack.isEmpty()){
                        return;
                    }
                }


                LivingEntity target = null;
                if (livingEntity instanceof Mob mob){
                    if (mob.getTarget() != null) target = mob.getTarget();
                    else if (mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) target = mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
                } else{
                    target = getLookedAtEntity(level, livingEntity);
                }

                if (target == null) {
                    var conditions = TargetingConditions.forCombat().range(12.0D).selector(e -> {
                        if (e == livingEntity) return false;
                        else if (e.isAlliedTo(livingEntity)) return false;
                        else if (e instanceof Enemy) return true;

                        if (e.getTeam() != null && !e.isAlliedTo(livingEntity)){
                            return true;
                        }
                        return false;
                    });
                    List<LivingEntity> targets = level.getNearbyEntities(LivingEntity.class, conditions, livingEntity,
                            livingEntity.getBoundingBox().inflate(8.0D, 4.0d, 8.0D));

                    if (targets.isEmpty()) return;

                    target = targets.get(level.random.nextInt(targets.size()));
                }

                Vec3 spawnPos = new Vec3(livingEntity.getX(), livingEntity.getY(1.2D), livingEntity.getZ());

                TorridWisp wisp = new TorridWisp(level, spawnPos.x, spawnPos.y, spawnPos.z);
                wisp.setOwner(livingEntity);
                wisp.setTarget(target);

                wisp.setAttackDelay(20);

                Vec3 randomVelocity = RandomUtil.randomPositionOnSphereGaussian(level.random, 0.15D).add(0.0D, 0.2D, 0.0D);
                wisp.setDeltaMovement(randomVelocity);

                level.playSound(null, BlockPos.containing(spawnPos), GladiusSoundEvents.TORRID_WISP_SPAWN.get(),
                        livingEntity.getSoundSource(), 1.0F, 0.8F + livingEntity.getRandom().nextFloat() * 0.3F);

                level.addFreshEntity(wisp);

                if (!ammoStack.isEmpty()){
                    draw(stack, ammoStack, livingEntity);
                }
            }
        }
    }

    @Nullable
    private static LivingEntity getLookedAtEntity(Level level, LivingEntity livingEntity){
        Vec3 eyePos = livingEntity.getEyePosition();
        Vec3 endPos = eyePos.add(livingEntity.getViewVector(1.0F).scale(12.0D));
        HitResult blockHitResult = level.clip(new ClipContext(
                eyePos, endPos,
                ClipContext.Block.VISUAL,
                ClipContext.Fluid.NONE,
                livingEntity
        ));
        if (blockHitResult.getType() == HitResult.Type.MISS){
            var entityHit = ProjectileUtil.getEntityHitResult(level, livingEntity, eyePos, endPos,
                    livingEntity.getBoundingBox().expandTowards(livingEntity.getViewVector(1.0F).scale(12.0D)),
                    e -> e instanceof LivingEntity && e.isAlive() && !e.isAlliedTo(livingEntity), 0.6F);
            if (entityHit != null && entityHit.getEntity() instanceof LivingEntity living){
                return living;
            }
        }
        return null;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
}
