package com.feliscape.gladius.foundation;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;

public class MobEffectRenderers {
    private static final Map<MobEffect, MobEffectRendererProvider> PROVIDERS = new Object2ObjectOpenHashMap<>();

    public static void register(Holder<? extends MobEffect> effect, MobEffectRendererProvider provider) {
        PROVIDERS.put(effect.value(), provider);
    }
    public static void register(MobEffect effect, MobEffectRendererProvider provider) {
        PROVIDERS.put(effect, provider);
    }

    public static Map<MobEffect, MobEffectRenderer> createRenderers(EntityRendererProvider.Context context) {
        ImmutableMap.Builder<MobEffect, MobEffectRenderer> builder = ImmutableMap.builder();
        PROVIDERS.forEach((effect, factory) -> {
            try {
                builder.put(effect, factory.create(context));
            } catch (Exception exception) {
                throw new IllegalArgumentException("Failed to create renderer for " + BuiltInRegistries.MOB_EFFECT.getKey(effect), exception);
            }
        });
        return builder.build();
    }
}
