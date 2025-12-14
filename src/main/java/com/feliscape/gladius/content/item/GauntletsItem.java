package com.feliscape.gladius.content.item;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.GauntletData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

public class GauntletsItem extends Item {
    public static final ResourceLocation BASE_ENTITY_INTERACTION_RANGE_ID = Gladius.location("base_entity_interaction_range");
    protected final int maxCombo;
    protected final int comboLifetime;

    public GauntletsItem(Properties properties) {
        this(properties, 20, 40);
    }
    public GauntletsItem(Properties properties, int maxCombo) {
        this(properties, maxCombo, 40);
    }
    public GauntletsItem(Properties properties, int maxCombo, int comboLifetime) {
        super(properties);
        this.maxCombo = maxCombo;
        this.comboLifetime = comboLifetime;
    }

    public static ItemAttributeModifiers createAttributes(double attackDamage, double attackSpeed, double entityInteractionRange) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        BASE_ATTACK_DAMAGE_ID,
                        attackDamage,
                        AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(
                        BASE_ATTACK_SPEED_ID,
                        attackSpeed,
                        AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(
                        BASE_ENTITY_INTERACTION_RANGE_ID,
                        entityInteractionRange,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ), EquipmentSlotGroup.MAINHAND)
                .build();
    }
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        var entity = damageSource.getEntity() == null ? damageSource.getDirectEntity() : damageSource.getEntity();
        if (entity == null || !entity.hasData(GauntletData.type())) return 0.0F;
        var data = entity.getData(GauntletData.type());
        return damage * data.getComboPercentage(maxCombo);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Gladius.LOGGER.debug("Hit entity with gauntlet");
        attacker.getData(GauntletData.type()).doCombo(this.maxCombo);
    }

    public int getMaxCombo(){
        return maxCombo;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.gladius.wip").withStyle(ChatFormatting.GRAY));
    }
}
