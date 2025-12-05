package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
    @Shadow public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> component, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag);

    @Shadow public abstract Item getItem();

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
    ordinal = 5), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void addAdditionalGlobalTooltips(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir,
                                            List<Component> list){
        if (!player.level().isClientSide || GladiusClientConfig.CONFIG.aspects.showAspectTooltips.getAsBoolean())
            this.addToTooltip(GladiusComponents.ASPECT.get(), tooltipContext, list::add, tooltipFlag);

        if (!ModList.get().isLoaded("bettercombat") && has(GladiusComponents.TWO_HANDED)) {
            list.add(Component.translatable("item.gladius.tooltip.two_handed").withStyle(ChatFormatting.GRAY));
        }
    }
}
