package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GladiusTags {
    public static class Blocks{

        private static TagKey<Block> create(String name){
            return TagKey.create(Registries.BLOCK, Gladius.location(name));
        }
    }
    public static class EntityTypes{
        public static final TagKey<EntityType<?>> STUN_IMMUNE = create("stun_immune");
        public static final TagKey<EntityType<?>> STAB_IMMUNE = create("stab_immune");

        public static final TagKey<EntityType<?>> BLEEDING_IMMUNE = create("bleeding_immune");

        private static TagKey<EntityType<?>> create(String name){
            return TagKey.create(Registries.ENTITY_TYPE, Gladius.location(name));
        }
    }
    public static class Items{
        public static final TagKey<Item> BLOCKING_ENCHANTABLE = create("blocking_enchantable");
        public static final TagKey<Item> INNATE_STUN = create("innate_stun");
        public static final TagKey<Item> TWO_HANDED = create("two_handed");

        private static TagKey<Item> create(String name){
            return TagKey.create(Registries.ITEM, Gladius.location(name));
        }
    }
    public static class MobEffects{
        public static final TagKey<MobEffect> NO_PARTICLE = create("no_particle");
        public static final TagKey<MobEffect> NO_FLICKER = create("no_flicker");

        private static TagKey<MobEffect> create(String name){
            return TagKey.create(Registries.MOB_EFFECT, Gladius.location(name));
        }
    }
}
