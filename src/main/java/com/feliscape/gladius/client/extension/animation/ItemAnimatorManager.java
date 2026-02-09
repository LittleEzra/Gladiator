package com.feliscape.gladius.client.extension.animation;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ItemAnimatorManager {
    static final Map<Item, CustomItemAnimator> ANIMATORS = new Reference2ObjectOpenHashMap<>();

    @SafeVarargs
    public static void register(CustomItemAnimator animator, Holder<Item>... items){
        register(animator, (Item[])Arrays.stream(items).map(Holder::value).toArray((x$0) -> new Item[x$0]));
    }
    public static void register(CustomItemAnimator animator, Item... items){
        if (items.length == 0) {
            throw new IllegalArgumentException("At least one target must be provided");
        }
        Objects.requireNonNull(animator, "Animator must not be null");

        for (Item object : items) {
            Objects.requireNonNull(items, "Target must not be null");
            CustomItemAnimator oldExtensions = ANIMATORS.put(object, animator);
            if (oldExtensions != null) {
                throw new IllegalStateException(String.format(
                        Locale.ROOT,
                        "Duplicate client animator registration for %s (old: %s, new: %s)",
                        object,
                        oldExtensions,
                        animator));
            }
        }
    }
}
