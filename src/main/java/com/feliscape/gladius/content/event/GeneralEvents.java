package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.BlackstoneGolem;
import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.feliscape.gladius.content.entity.Frostmancer;
import com.feliscape.gladius.content.entity.ai.RetrieveArrowGoal;
import com.feliscape.gladius.content.entity.ai.ReturnArrowGoal;
import com.feliscape.gladius.content.entity.misc.FireWake;
import com.feliscape.gladius.content.item.WandItem;
import com.feliscape.gladius.registry.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GeneralEvents {
    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        event.put(GladiusEntityTypes.CRYSTAL_BUTTERFLY.get(), CrystalButterfly.createAttributes().build());
        event.put(GladiusEntityTypes.FROSTMANCER.get(), Frostmancer.createAttributes().build());
        event.put(GladiusEntityTypes.BLACKSTONE_GOLEM.get(), BlackstoneGolem.createAttributes().build());
    }


    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event){
        Level level = event.getEntity().level();
        if (event.getEntity() instanceof LivingEntity living){
            ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);
            if (living.onGround() && boots.is(GladiusItems.FLAMEWALKERS)){
                if (!level.isClientSide) {
                    if (!living.isSprinting()) return;

                    var flamewalkersHeat = boots.get(GladiusComponents.HEAT);
                    if (flamewalkersHeat == null) return;

                    var heat = flamewalkersHeat.mutable();
                    double velocity = living.getKnownMovement().length();

                    int interval = Math.max((int) (6 - velocity * 12), 0) + 1;
                    if (living.tickCount % interval == 0) {

                        if (heat.canBurn()){
                            FireWake fireWake = new FireWake(level, living.getX(), living.getY(), living.getZ(), living);
                            level.addFreshEntity(fireWake);
                            heat.decreaseHeat(1);
                        } else{
                            heat.increaseHeat(1);
                        }

                        boots.set(GladiusComponents.HEAT, heat.toImmutable());
                    }
                } else{
                    if (level.random.nextInt(3) == 0) {
                        level.addParticle(GladiusParticles.MAGMA_TRAIL.get(),
                                living.getRandomX(0.5D), living.getY() + level.random.nextFloat() * 0.01D, living.getRandomZ(0.5D),
                                0.0D, 0.0D, 0.0D);
                    } else{
                        level.addParticle(ParticleTypes.FLAME,
                                living.getRandomX(0.5D), living.getY(), living.getRandomZ(0.5D),
                                0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void entitySpawn(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if (entity.isAlive() && event.getEntity() instanceof Wolf wolf){
            wolf.goalSelector.addGoal(4, new ReturnArrowGoal(wolf));
            wolf.goalSelector.addGoal(6, new RetrieveArrowGoal(wolf));
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event){
        Entity cause = event.getSource().getDirectEntity();
        if (!(cause instanceof LivingEntity livingAttacker)) return;

        if (livingAttacker instanceof Player player && event.getAmount() >= 1.99F){
            boolean rechargedManaWeapons = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++){
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.has(GladiusComponents.MAGIC_CHARGES)){
                    int charges = WandItem.getCharges(itemStack);
                    int maxCharges = WandItem.getMaxCharges(itemStack);
                    if (charges < maxCharges) {
                        rechargedManaWeapons = true;
                        WandItem.setCharges(itemStack, Math.min(charges + Mth.floor(Mth.sqrt(event.getAmount())), maxCharges));
                    }
                }
            }
        }
    }
}
