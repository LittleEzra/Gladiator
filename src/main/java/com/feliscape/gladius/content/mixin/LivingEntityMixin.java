package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.content.item.CustomShieldExtension;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public boolean hasEffect(Holder<MobEffect> effect) {
        throw new AbstractMethodError("Shadow called directly");
    }

    @Shadow public abstract ItemStack getUseItem();

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void onCanAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (hasEffect(GladiusMobEffects.STUN) || hasEffect(GladiusMobEffects.FLASHED)) cir.setReturnValue(false);
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "HEAD"), cancellable = true)
    public void handleEntityEvent(byte id, CallbackInfo ci){
        if (id == 29 && getUseItem().getItem() instanceof CustomShieldExtension customShield){
            this.playSound(customShield.getSound(), 1.0F, 0.9F + this.level().random.nextFloat() * 0.2F);
            ci.cancel();
        }
    }
}
