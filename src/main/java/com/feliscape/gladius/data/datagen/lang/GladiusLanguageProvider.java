package com.feliscape.gladius.data.datagen.lang;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.advancement.CustomAdvancement;
import com.feliscape.gladius.data.element.Aspect;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class GladiusLanguageProvider extends LanguageProvider {
    public GladiusLanguageProvider(PackOutput output, String locale) {
        super(output, Gladius.MOD_ID, locale);
    }

    protected void addBlockAndItem(Supplier<? extends Block> key, String name) {
        this.addBlock(key, name);
        this.addItem(key.get()::asItem, name);
    }

    protected void addItemTooltip(Supplier<? extends Item> key, String name) {
        add(key.get().getDescriptionId() + ".tooltip", name);
    }
    protected void addDeathMessage(ResourceKey<DamageType> key, String message) {
        add("death.attack.%s".formatted(key.location().toString()), message);
    }
    protected void addDeathMessageItem(ResourceKey<DamageType> key, String message) {
        add("death.attack.%s.item".formatted(key.location().toString()), message);
    }
    protected void addDeathMessagePlayer(ResourceKey<DamageType> key, String message) {
        add("death.attack.%s.player".formatted(key.location().toString()), message);
    }
    protected void addMobEffect(Holder<? extends MobEffect> key, String name) {
        add(key.value().getDescriptionId(), name);
    }
    protected void addSubtitle(Supplier<SoundEvent> key, String name) {
        add("subtitle.%s.%s".formatted(Gladius.MOD_ID, key.get().getLocation().getPath()), name);
    }
    protected void addAdvancement(String id, String title, String description) {
        add("advancements.%s.%s.title".formatted(Gladius.MOD_ID, id), title);
        add("advancements.%s.%s.description".formatted(Gladius.MOD_ID, id), description);
    }
    protected void addAdvancement(CustomAdvancement advancement, String title, String description) {
        if (advancement.getTitle().getContents() instanceof TranslatableContents translatable)
            add(translatable.getKey(), title);
        if (advancement.getDescription().getContents() instanceof TranslatableContents translatable)
            add(translatable.getKey(), description);
    }
    protected void addAspect(ResourceKey<Aspect> key, String name) {
        add(Util.makeDescriptionId("aspect", key.location()), name);
    }
    protected void addEnchantment(ResourceKey<Enchantment> key, String name) {
        add(Util.makeDescriptionId("enchantment", key.location()), name);
    }
    protected void addBiome(ResourceKey<Biome> key, String name) {
        add(Util.makeDescriptionId("biome", key.location()), name);
    }
    protected void addTab(Supplier<CreativeModeTab> key, String name) {
        add(Util.makeDescriptionId("itemGroup", BuiltInRegistries.CREATIVE_MODE_TAB.getKey(key.get())), name);
    }
    protected void addAttribute(Holder<Attribute> key, String name) {
        add(key.value().getDescriptionId(), name);
    }

    protected void addConfigSection(String section, String name){
        add(Gladius.MOD_ID + ".configuration." + section, name);
    }
    protected void addConfigValue(String id, String name){
        add(Gladius.MOD_ID + ".configuration." + id, name);
    }
    protected void addConfigValue(String environment, ModConfigSpec.ConfigValue<?> value, String name){
        add("%s.configuration.%s.%s".formatted(Gladius.MOD_ID, environment, combineConfigValuePath(value.getPath())), name);
    }

    protected void addPotion(Holder<Potion> key, String name) {
        add(Potion.getName(Optional.of(key), "item.minecraft.potion.effect."), name);
    }
    protected void addTippedArrow(Holder<Potion> key, String name) {
        add(Potion.getName(Optional.of(key), "item.minecraft.tipped_arrow.effect."), name);
    }
    protected void addSplashPotion(Holder<Potion> key, String name) {
        add(Potion.getName(Optional.of(key), "item.minecraft.splash_potion.effect."), name);
    }
    protected void addLingeringPotion(Holder<Potion> key, String name) {
        add(Potion.getName(Optional.of(key), "item.minecraft.lingering_potion.effect."), name);
    }

    protected String combineConfigValuePath(List<String> path){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < path.size(); i++){
            string.append(path.get(i));
            if (i < path.size() - 1){
                string.append(".");
            }
        }
        return string.toString();
    }
}
