package com.feliscape.gladius.content.item;

import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;
import java.util.List;

public class NightwalkerArmorItem extends ArmorItem {
    public NightwalkerArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public static boolean isInStealth(@Nullable LivingEntity living){
        if (living == null) return false;
        return (living.getPose() == Pose.CROUCHING || living.isShiftKeyDown()) && living.getItemBySlot(EquipmentSlot.HEAD).is(GladiusItems.NIGHTWALKER_HOOD);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.gladius.wip").withStyle(ChatFormatting.GRAY));
    }
}
