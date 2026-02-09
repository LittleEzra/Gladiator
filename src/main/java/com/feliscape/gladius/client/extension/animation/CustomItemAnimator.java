package com.feliscape.gladius.client.extension.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class CustomItemAnimator {
    static final CustomItemAnimator DEFAULT = new CustomItemAnimator() {};

    @Nullable
    public static CustomItemAnimator of(ItemStack itemStack){
        return of(itemStack.getItem());
    }

    @Nullable
    public static CustomItemAnimator of(Item item) {
        return ItemAnimatorManager.ANIMATORS.get(item);
    }

    /**
     * The item's transform in third person
     * @return true if the item was rendered manually, skipping vanilla rendering
     */
    public boolean renderThirdPerson(ItemStack itemStack,
                                     ItemDisplayContext displayContext,
                                     LivingEntity entity,
                                     PoseStack poseStack,
                                     HumanoidArm arm,
                                     MultiBufferSource buffers,
                                     int packedLight,
                                     ItemInHandRenderer itemInHandRenderer,
                                     ArmedModel model
    ){
        return false;
    }

    public boolean renderFirstPerson(ItemStack itemStack,
                                     AbstractClientPlayer player,
                                     PoseStack poseStack,
                                     HumanoidArm arm,
                                     MultiBufferSource buffers,
                                     int packedLight,
                                     float equipProgress,
                                     float swingProgress,
                                     ItemInHandRenderer itemInHandRenderer
    ){
        return false;
    }

    public boolean poseArms(LivingEntity entity, HumanoidModel<?> model, HumanoidArm arm){
        return false;
    }

    public boolean twoHanded(){
        return false;
    }
    public boolean hideOtherHand(LivingEntity entity, InteractionHand hand){
        return false;
    }

    public static ModelPart getArmPart(HumanoidModel<?> model, HumanoidArm arm){
        return arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
    }
    public static InteractionHand getHand(LivingEntity entity, HumanoidArm arm){
        if (arm == entity.getMainArm()){
            return InteractionHand.MAIN_HAND;
        }
        return InteractionHand.OFF_HAND;
    }

    public static boolean isUsingArm(LivingEntity entity, HumanoidArm arm){
        if (!entity.isUsingItem()) return false;
        boolean mainArm = entity.getMainArm() == arm;
        boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        return mainArm == usingMainHand;
    }
}
