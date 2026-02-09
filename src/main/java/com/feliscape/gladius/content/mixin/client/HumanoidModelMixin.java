package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.client.extension.animation.CustomItemAnimator;
import com.feliscape.gladius.content.attachment.PowerGauntletData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {
    @Unique
    @SuppressWarnings("unchecked")
    private HumanoidModel<T> self = (HumanoidModel<T>) (Object) this;

    @Shadow protected abstract HumanoidArm getAttackArm(T entity);

    @Shadow protected abstract ModelPart getArm(HumanoidArm side);

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

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

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/world/entity/LivingEntity;F)V"))
    public void overrideArmPose(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){

        ItemStack offhand = entity.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack mainhand = entity.getItemInHand(InteractionHand.MAIN_HAND);

        CustomItemAnimator offhandAnimator = CustomItemAnimator.of(offhand);
        CustomItemAnimator mainhandAnimator = CustomItemAnimator.of(mainhand);
        // Animate the offhand item first so that the mainhand item can override the offhand
        if (offhandAnimator != null){
            offhandAnimator.poseArms(entity, self, entity.getMainArm().getOpposite());
        }
        if (mainhandAnimator != null){
            mainhandAnimator.poseArms(entity, self, entity.getMainArm());
        }

        /*HumanoidArm arm = entity.getMainArm();
        ItemStack mainHandItem = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        CustomItemAnimator mainAnimator = CustomItemAnimator.of(mainHandItem);
        if (mainAnimator != null && mainAnimator.twoHanded()){
            mainAnimator.poseRightArm(entity, self, this.rightArm);
            mainAnimator.poseLeftArm(entity, self, this.rightArm);
        } else{
            ItemStack itemStack = entity.getUseItem();
            CustomItemAnimator animator = CustomItemAnimator.of(itemStack);
            if (animator == null) return;

            boolean mainArmIsRight = entity.getMainArm() == HumanoidArm.RIGHT;
            boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (usingMainHand == mainArmIsRight) {
                animator.poseRightArm(entity, self, this.rightArm);
            } else {
                animator.poseLeftArm(entity, self, this.leftArm);
            }
        }*/
    }

    /*@Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    public void overrideRightArmPose(T livingEntity, CallbackInfo ci){
        ItemStack stack = livingEntity.getMainArm() == HumanoidArm.RIGHT ? livingEntity.getItemBySlot(EquipmentSlot.MAINHAND) : livingEntity.getItemBySlot(EquipmentSlot.OFFHAND);
        if (!stack.isEmpty()){
            var animator = CustomItemAnimator.of(stack);
            if (animator != null && animator.poseRightArm(livingEntity, self, this.rightArm)){
                ci.cancel();
            }
        }
    }
    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    public void overrideLeftArmPose(T livingEntity, CallbackInfo ci){
        ItemStack stack = livingEntity.getMainArm() == HumanoidArm.LEFT ? livingEntity.getItemBySlot(EquipmentSlot.MAINHAND) : livingEntity.getItemBySlot(EquipmentSlot.OFFHAND);
        if (!stack.isEmpty()){
            var animator = CustomItemAnimator.of(stack);
            if (animator != null && animator.poseLeftArm(livingEntity, self, this.leftArm)){
                ci.cancel();
            }
        }
    }*/
}
