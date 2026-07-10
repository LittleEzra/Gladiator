package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.content.item.NightwalkerArmorItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private final Entity self = (Entity) ((Object)this);

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    public void hideWhenNightwalking(CallbackInfoReturnable<Boolean> cir){
        if (self instanceof LivingEntity living && NightwalkerArmorItem.isInStealth(living)){
            cir.setReturnValue(true);
        }
    }
}
