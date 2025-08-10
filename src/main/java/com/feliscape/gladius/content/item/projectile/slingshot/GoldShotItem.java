package com.feliscape.gladius.content.item.projectile.slingshot;

import com.feliscape.gladius.content.entity.projectile.slingshot.GoldShot;
import com.feliscape.gladius.content.entity.projectile.slingshot.SlingshotProjectile;
import com.feliscape.gladius.content.entity.projectile.slingshot.SteelShot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoldShotItem extends SlingshotAmmoItem{

    public GoldShotItem(Properties properties) {
        super(properties);
    }

    @Override
    public SlingshotProjectile createProjectile(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new GoldShot(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new GoldShot(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("WIP").withStyle(ChatFormatting.GRAY));
    }
}
