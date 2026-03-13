package com.feliscape.gladius.content.item;

import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import com.feliscape.gladius.util.EntityUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class HoglinTuskItem extends InstrumentItem {
    public HoglinTuskItem(Properties properties) {
        super(properties, GladiusTags.Instruments.HOGLIN_TUSK);
    }


    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration == getUseDuration(stack, livingEntity) - 1){
            List<LivingEntity> nearbyAllies = level.getEntitiesOfClass(LivingEntity.class,
                    livingEntity.getBoundingBox().inflate(12.0D, 8.0D, 12.0D),
                    entity -> validateAlly(entity, livingEntity));

            for (LivingEntity entity : nearbyAllies){
                entity.addEffect(new MobEffectInstance(GladiusMobEffects.BATTLE_CRY, 15 * 20));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var superResult = super.use(level, player, usedHand);
        /*if (superResult.getResult() == InteractionResult.CONSUME){
            List<LivingEntity> nearbyAllies = level.getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(12.0D, 8.0D, 12.0D),
                    entity -> validateAlly(entity, player));

            for (LivingEntity entity : nearbyAllies){
                entity.addEffect(new MobEffectInstance(GladiusMobEffects.BATTLE_CRY, 30 * 20));
            }
        }*/
        return superResult;
    }


    private static boolean validateAlly(LivingEntity entity, LivingEntity user){
        if (entity == null) return false;
        if (entity == user) return true;

        if (EntityUtil.areAllied(entity, user)) return true;
        else if (user instanceof Player && entity instanceof Player) return true;
        else if (entity instanceof OwnableEntity ownable) return validateAllyOwner(ownable.getOwner(), user);
        return false;
    }


    /// Identical to validateAlly, except that it doesn't check owners, to avoid potential infinite loops
    private static boolean validateAllyOwner(LivingEntity entity, LivingEntity user){
        if (entity == null) return false;
        if (entity == user) return true;

        if (EntityUtil.areAllied(entity, user)) return true;
        else if (user instanceof Player && entity instanceof Player) return true;
        return false;
    }
}
