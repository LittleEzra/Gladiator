package com.feliscape.gladius.data.datagen.lang;

import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.GladiusServerConfig;
import com.feliscape.gladius.data.damage.GladiusDamageTypes;
import com.feliscape.gladius.data.datagen.advancement.GladiusAdvancements;
import com.feliscape.gladius.data.enchantment.GladiusEnchantments;
import com.feliscape.gladius.data.registry.GladiusAspects;
import com.feliscape.gladius.registry.*;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;

public class GladiusEnUsProvider extends GladiusLanguageProvider{
    public GladiusEnUsProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(GladiusBlocks.FRIGID_ICE, "Frigid Ice");

        this.addItem(GladiusItems.FROSTMANCER_SPAWN_EGG, "Frostmancer Spawn Egg");

        this.addItem(GladiusItems.BLAZING_HEART, "Blazing Heart");
        this.addItem(GladiusItems.FRIGID_SEED, "Frigid Seed");
        this.addItemTooltip(GladiusItems.FRIGID_SEED, "Can be planted inside of Blue Ice");
        this.addItem(GladiusItems.FRIGID_SHARD, "Frigid Shard");
        this.addItem(GladiusItems.HEARTH_STONE, "Hearth Stone");

        this.addItem(GladiusItems.EXPLOSIVE_ARROW, "Explosive Arrow");
        this.add("item.nuanced_combat.explosive_arrow.power", "Power: %1$s");
        this.addItem(GladiusItems.PRISMARINE_ARROW, "Prismarine Arrow");
        this.addItem(GladiusItems.WINGED_ARROW, "Winged Arrow");

        this.addItem(GladiusItems.OIL_BOTTLE, "Oil Bottle");
        this.addItem(GladiusItems.FIREBRAND, "Firebrand");
        this.addItem(GladiusItems.ICE_BOMB, "Ice Bomb");

        this.addItem(GladiusItems.FLASH_POWDER, "Flash Powder");
        this.addItem(GladiusItems.CRYSTAL_BUTTERFLY, "Crystal Butterfly");

        this.addItem(GladiusItems.GILDED_DAGGER, "Gilded Dagger");
        this.addItem(GladiusItems.CLAYMORE, "Claymore");
        this.addItem(GladiusItems.FLAMBERGE, "Flamberge");
        this.addItem(GladiusItems.GOLDEN_WAND, "Golden Wand");
        this.add("item.gladius.tooltip.two_handed", "Two-Handed");

        this.addAspect(GladiusAspects.FIRE, "🔥 Fire");
        this.addAspect(GladiusAspects.MAGIC, "🧪 Magic");
        this.addAspect(GladiusAspects.ICE, "❄ Ice");
        this.addAspect(GladiusAspects.POISON, "☠ Poison");

        this.addEntityType(GladiusEntityTypes.MAGIC_ORB, "Magic Orb");
        this.addEntityType(GladiusEntityTypes.EXPLOSIVE_ARROW, "Explosive Arrow");
        this.addEntityType(GladiusEntityTypes.PRISMARINE_ARROW, "Prismarine Arrow");
        this.addEntityType(GladiusEntityTypes.WINGED_ARROW, "Winged Arrow");
        this.addEntityType(GladiusEntityTypes.OIL_BOTTLE, "Oil Bottle");
        this.addEntityType(GladiusEntityTypes.FIREBRAND, "Firebrand");

        this.addEntityType(GladiusEntityTypes.ICE_BOMB, "Ice Bomb");
        this.addEntityType(GladiusEntityTypes.ICE_SPIKE_SPAWNER, "Ice Spike Spawner");
        this.addEntityType(GladiusEntityTypes.ICE_SPIKE, "Ice Spike");
        this.addEntityType(GladiusEntityTypes.ICE_BLOCK, "Ice Block");
        this.addEntityType(GladiusEntityTypes.FROSTMANCER, "Frostmancer");

        this.addEntityType(GladiusEntityTypes.FLASH_POWDER_CLOUD, "Flash Powder Cloud");
        this.addEntityType(GladiusEntityTypes.CRYSTAL_BUTTERFLY, "Crystal Butterfly");

        this.addMobEffect(GladiusMobEffects.STUN, "Stun");
        this.addMobEffect(GladiusMobEffects.OVERBURDENED, "Overburdened");
        this.addMobEffect(GladiusMobEffects.BLEEDING, "Bleeding");
        this.addMobEffect(GladiusMobEffects.FLAMMABLE, "Flammable");
        this.addMobEffect(GladiusMobEffects.FLASHED, "Flashed");
        this.addMobEffect(GladiusMobEffects.FREEZING, "Freezing");
        this.addMobEffect(GladiusMobEffects.FROST_RESISTANCE, "Frost Resistance");

        this.addEnchantment(GladiusEnchantments.STUNNING, "Stunning");

        this.addSubtitle(GladiusSoundEvents.CLAYMORE_BLOCK, "Claymore blocks");
        this.addSubtitle(GladiusSoundEvents.GILDED_DAGGER_STAB, "Gilded Dagger stabs");
        this.addSubtitle(GladiusSoundEvents.FIREBRAND_LIGHT, "Firebrand lights fire");
        this.addSubtitle(GladiusSoundEvents.FLASH_POWDER_CRACKLE, "Flash Powder crackles");
        this.addSubtitle(GladiusSoundEvents.HEARTH_STONE_USE, "Hearth Stone ignites");

        this.addSubtitle(GladiusSoundEvents.SPELL, "Spell is cast");

        this.addSubtitle(GladiusSoundEvents.ICE_BOMB_THROW, "Ice Bomb flies");
        this.addSubtitle(GladiusSoundEvents.ICE_BOMB_SHATTER, "Ice Bomb explodes");
        this.addSubtitle(GladiusSoundEvents.ICE_SPIKE_RISE, "Ice Spike rise");
        this.addSubtitle(GladiusSoundEvents.ICE_BLOCK_SHATTER, "Ice Block shatters");

        this.addSubtitle(GladiusSoundEvents.FROSTMANCER_SHIELD_BREAK, "Frostmancer's shield breaks");

        this.addSubtitle(GladiusSoundEvents.FRIGID_ICE_FREEZE, "Frigid Ice spreads");

        this.addDeathMessage(GladiusDamageTypes.BLEEDING, "%1$s bled out");

        this.addDeathMessage(GladiusDamageTypes.MAGIC_PROJECTILE, "%1$s was shot by %2$s");
        this.addDeathMessageItem(GladiusDamageTypes.MAGIC_PROJECTILE, "%1$s was shot by %2$s using %3$s");

        this.addDeathMessage(GladiusDamageTypes.INDIRECT_SKEWERING, "%1$s was skewered by %2$s");
        this.addDeathMessageItem(GladiusDamageTypes.INDIRECT_SKEWERING, "%1$s was skewered by %2$s using %3$s");
        this.addDeathMessage(GladiusDamageTypes.SKEWERING, "%1$s was skewered");
        this.addDeathMessagePlayer(GladiusDamageTypes.SKEWERING, "%1$s was skewered whilst trying to escape %2$s");

        this.addPotion(GladiusPotions.FROST_RESISTANCE, "Potion of Frost Resistance");
        this.addSplashPotion(GladiusPotions.FROST_RESISTANCE, "Splash Potion of Frost Resistance");
        this.addLingeringPotion(GladiusPotions.FROST_RESISTANCE, "Lingering Potion of Frost Resistance");
        this.addTippedArrow(GladiusPotions.FROST_RESISTANCE, "Arrow of Frost Resistance");

        this.addAdvancement(GladiusAdvancements.FIREBRAND,
                "Pyromaniac",
                "Throw a Firebrand with zero consideration for your surroundings");
        this.addAdvancement(GladiusAdvancements.OIL_BOTTLE,
                "Kitchen Nightmares",
                "Get a bottle of Oil from a Villager");
        this.addAdvancement(GladiusAdvancements.POCKET_SAND,
                "Pocket Sand!",
                "Toss Flash Powder into the air to confuse your enemies");

        this.addConfigSection("effects", "Effects");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.stunTrailResolution, "Stun Trail Resolution");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.showBlood, "Show Blood");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.extraOil, "Extra Oil");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.oilSplatDisappearStyle, "Oil Splat Disappear Effect");
        this.addConfigSection("flash_powder", "Flash Powder");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowder.flashPowderFlashing, "Flash Powder Flashing");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowder.flashPowderLightChance, "Flash Powder Light Chance");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowder.darkFlash, "Dark Flash");
        this.addConfigSection("aspects", "Aspects");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.aspects.showAspectTooltips, "Show Aspect Tooltips");

        this.addConfigSection("entities", "Entities");
        this.addConfigValue("server", GladiusServerConfig.CONFIG.crystalButterflySearchRange, "Crystal Butterfly Search Range");
        this.addConfigSection("projectiles", "Projectiles");
        this.addConfigValue("server", GladiusServerConfig.CONFIG.firebrandMakesFire, "Firebrand Makes Fire");
    }
}
