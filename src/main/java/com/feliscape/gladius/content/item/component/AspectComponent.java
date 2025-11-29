package com.feliscape.gladius.content.item.component;

import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.registry.GladiusDatapackRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipProvider;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Consumer;

public record AspectComponent(EitherHolder<Aspect> aspect, boolean affectAttack) implements TooltipProvider {
    public static final Codec<AspectComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EitherHolder.codec(GladiusDatapackRegistries.ASPECT, Aspect.CODEC).fieldOf("aspect").forGetter(AspectComponent::aspect),
            Codec.BOOL.fieldOf("affect_attack").forGetter(AspectComponent::affectAttack)
    ).apply(instance, AspectComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AspectComponent> STREAM_CODEC = StreamCodec.composite(
            EitherHolder.streamCodec(GladiusDatapackRegistries.ASPECT, Aspect.STREAM_CODEC),
            AspectComponent::aspect,
            ByteBufCodecs.BOOL,
            AspectComponent::affectAttack,
            AspectComponent::new
    );

    public static AspectComponent of(ResourceKey<Aspect> aspectResourceKey, boolean affectAttack){
        return new AspectComponent(new EitherHolder<>(aspectResourceKey), affectAttack);
    }
    public static AspectComponent of(Holder<Aspect> aspectHolder, boolean affectAttack){
        return new AspectComponent(new EitherHolder<>(aspectHolder), affectAttack);
    }

    public Optional<Holder<Aspect>> optionalAspect(){
        return aspect.holder();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        var registries = context.registries();
        if (registries != null) {
            Optional<Holder<Aspect>> optionalAspect = aspect().unwrap(registries);
            if (optionalAspect.isPresent()){
                tooltipAdder.accept(Component.translatable(Util.makeDescriptionId("aspect", aspect().key().location()))
                        .withColor(optionalAspect.get().value().color()));
            }
        }
    }
}
