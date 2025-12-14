package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.content.attachment.PowerGauntletData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {
    @Shadow protected abstract HumanoidArm getAttackArm(T entity);

    @Shadow protected abstract ModelPart getArm(HumanoidArm side);

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if (entity.hasData(PowerGauntletData.type())){
            var data = entity.getData(PowerGauntletData.type());
            if (data.isProjectile()){
                var arm = getArm(getAttackArm(entity));
                arm.xRot = -Mth.HALF_PI;
            }
        }
    }
}
