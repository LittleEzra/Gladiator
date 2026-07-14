package com.feliscape.gladius.client.extension;

import com.feliscape.gladius.content.item.RodBowItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class RodBowClientExtensions implements IClientItemExtensions {
    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        if (RodBowItem.isCharged(itemStack)) return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        return null;
    }
}
