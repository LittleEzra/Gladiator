package com.feliscape.gladius.content.item.projectile.arrow;

import com.feliscape.gladius.content.entity.projectile.ExplosiveArrow;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExplosiveArrowItem extends ArrowItem {
    public ExplosiveArrowItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ExplosiveArrow(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ExplosiveArrow arrow = new ExplosiveArrow(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    public static int getStrength(ItemStack itemStack){
        var power = itemStack.get(GladiusComponents.POWER);
        return power == null ? 1 : power;
    }

    public static void setStrength(ItemStack pStack, int power) {
        pStack.set(GladiusComponents.POWER, power);
    }
    public static ItemStack forStrength(int power) {
        ItemStack itemStack = new ItemStack(GladiusItems.EXPLOSIVE_ARROW.get(), 1);
        itemStack.set(GladiusComponents.POWER, power);
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        var power = stack.get(GladiusComponents.POWER);
        if (power != null) {
            tooltipComponents.add(Component.translatable("item.nuanced_combat.explosive_arrow.power", power)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
