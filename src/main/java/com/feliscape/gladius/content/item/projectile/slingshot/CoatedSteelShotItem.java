package com.feliscape.gladius.content.item.projectile.slingshot;

import com.feliscape.gladius.content.entity.projectile.slingshot.CoatedSteelShot;
import com.feliscape.gladius.content.entity.projectile.slingshot.SlingshotProjectile;
import com.feliscape.gladius.content.entity.projectile.slingshot.SteelShot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoatedSteelShotItem extends SlingshotAmmoItem {
    public CoatedSteelShotItem(Properties properties) {
        super(properties);
    }

    @Override
    public SlingshotProjectile createProjectile(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new CoatedSteelShot(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new CoatedSteelShot(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
        return itemstack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("WIP").withStyle(ChatFormatting.GRAY));
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null) {
            potioncontents.addPotionTooltip(tooltipComponents::add, 0.125F, context.tickRate());
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different names based on their damage or NBT.
     */
    @Override
    public String getDescriptionId(ItemStack stack) {
        return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
    }
}
