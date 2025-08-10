package com.feliscape.gladius.data.datagen.lang;

import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.GladiusServerConfig;
import com.feliscape.gladius.data.damage.GladiusDamageTypes;
import com.feliscape.gladius.data.datagen.advancement.GladiusAdvancements;
import com.feliscape.gladius.data.enchantment.GladiusEnchantments;
import com.feliscape.gladius.registry.*;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class GladiusEnUsProvider extends GladiusLanguageProvider{
    public GladiusEnUsProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(GladiusItems.EXPLOSIVE_ARROW, "Explosive Arrow");
        this.add("item.nuanced_combat.explosive_arrow.power", "Power: %1$s");
        this.addItem(GladiusItems.PRISMARINE_ARROW, "Prismarine Arrow");
        this.addItem(GladiusItems.WINGED_ARROW, "Winged Arrow");

        this.addItem(GladiusItems.SLINGSHOT, "Slingshot");
        this.addItem(GladiusItems.STEEL_SHOT, "Steel Shot");
        this.addItem(GladiusItems.GOLD_SHOT, "Gold Shot");

        this.addItem(GladiusItems.OIL_BOTTLE, "Oil Bottle");
        this.addItem(GladiusItems.FIREBRAND, "Firebrand");

        this.addItem(GladiusItems.FLASH_POWDER, "Flash Powder");
        this.addItem(GladiusItems.CRYSTAL_BUTTERFLY, "Crystal Butterfly");

        this.addItem(GladiusItems.GILDED_DAGGER, "Gilded Dagger");
        this.addItem(GladiusItems.CLAYMORE, "Claymore");
        this.add("item.gladius.tooltip.two_handed", "Two-Handed");

        this.addEntityType(GladiusEntityTypes.EXPLOSIVE_ARROW, "Explosive Arrow");
        this.addEntityType(GladiusEntityTypes.PRISMARINE_ARROW, "Prismarine Arrow");
        this.addEntityType(GladiusEntityTypes.WINGED_ARROW, "Winged Arrow");
        this.addEntityType(GladiusEntityTypes.OIL_BOTTLE, "Oil Bottle");
        this.addEntityType(GladiusEntityTypes.FIREBRAND, "Firebrand");

        this.addEntityType(GladiusEntityTypes.STEEL_SHOT, "Steel Shot");

        this.addEntityType(GladiusEntityTypes.FLASH_POWDER_CLOUD, "Flash Powder Cloud");

        this.addMobEffect(GladiusMobEffects.STUN, "Stun");
        this.addMobEffect(GladiusMobEffects.OVERBURDENED, "Overburdened");
        this.addMobEffect(GladiusMobEffects.BLEEDING, "Bleeding");
        this.addMobEffect(GladiusMobEffects.FLAMMABLE, "Flammable");
        this.addMobEffect(GladiusMobEffects.FLASHED, "Flashed");

        this.addEnchantment(GladiusEnchantments.STUNNING, "Stunning");

        this.addSubtitle(GladiusSoundEvents.CLAYMORE_BLOCK, "Claymore blocks");
        this.addSubtitle(GladiusSoundEvents.FIREBRAND_LIGHT, "Firebrand lights fire");
        this.addSubtitle(GladiusSoundEvents.FLASH_POWDER_CRACKLE, "Flash Powder crackles");

        this.addTab(GladiusCreativeModeTabs.BASE_CREATIVE_TAB, "Gladius");

        this.addDeathMessage(GladiusDamageTypes.BLEEDING, "%1$s bled out");

        this.addAdvancement(GladiusAdvancements.FIREBRAND,
                "Pyromaniac",
                "Throw a Firebrand with zero consideration for your surroundings");
        this.addAdvancement(GladiusAdvancements.OIL_BOTTLE,
                "Kitchen Nightmares",
                "Get a bottle of Oil from a Villager");
        this.addAdvancement(GladiusAdvancements.POCKET_SAND,
                "Pocket Sand!",
                "Toss Flash Powder into the air to confuse your enemies");

        this.addItem(GladiusItems.COATED_STEEL_SHOT, "Coated Steel Shot");
        this.add("item.gladius.coated_steel_shot.effect.awkward", "Coated Steel Shot");
        this.add("item.gladius.coated_steel_shot.effect.empty", "Uncraftable Coated Steel Shot");
        this.addSteelShot(Potions.FIRE_RESISTANCE, "Fire-Resistance");
        this.addSteelShot(Potions.HARMING, "Harming");
        this.addSteelShot(Potions.HEALING, "Healing");
        this.addSteelShot(Potions.INFESTED, "Infested");
        this.addSteelShot(Potions.INVISIBILITY, "Invisibility");
        this.addSteelShot(Potions.LEAPING, "Leaping");
        this.add("item.gladius.coated_steel_shot.effect.levitation", "Levitation-Coated Steel Shot");
        this.addSteelShot(Potions.LUCK, "Luck");
        this.add("item.gladius.coated_steel_shot.effect.mundane", "Coated Steel Shot");
        this.addSteelShot(Potions.NIGHT_VISION, "Night-Vision");
        this.addSteelShot(Potions.OOZING, "Oozing");
        this.addSteelShot(Potions.POISON, "Poison");
        this.addSteelShot(Potions.REGENERATION, "Regeneration");
        this.addSteelShot(Potions.SLOW_FALLING, "Slow-Falling");
        this.addSteelShot(Potions.SLOWNESS, "Slowness");
        this.addSteelShot(Potions.STRENGTH, "Strength");
        this.addSteelShot(Potions.SWIFTNESS, "Swiftness");
        this.add("item.gladius.coated_steel_shot.effect.thick", "Coated Steel Shot");
        this.addSteelShot(Potions.TURTLE_MASTER, "Turtle-Master");
        this.addSteelShot(Potions.WATER, "Water");
        this.addSteelShot(Potions.WATER_BREATHING, "Water-Breathing");
        this.addSteelShot(Potions.WEAVING, "Weaving");
        this.addSteelShot(Potions.WIND_CHARGED, "Wind-Charged");

        this.addConfigSection("effects", "Effects");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.stunTrailResolution, "Stun Trail Resolution");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.showBlood, "Show Blood");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.extraOil, "Extra Oil");
        this.addConfigSection("flash_powder", "Flash Powder");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowderFlashing, "Flash Powder Flashing");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowderLightChance, "Flash Powder Light Chance");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.darkFlash, "Dark Flash");

        this.addConfigSection("projectiles", "Projectiles");
        this.addConfigValue("server", GladiusServerConfig.CONFIG.firebrandMakesFire, "Firebrand Makes Fire");
    }

    protected void addPotion(Holder<Potion> key, String name) {
        String location = ResourceLocation.parse(key.getRegisteredName()).getPath();
        add("item.minecraft.tipped_arrow.effect.%s".formatted(location), "Arrow of " + name);
        add("item.minecraft.potion.effect.%s".formatted(location), "Potion of " + name);
        add("item.minecraft.splash_potion.effect.%s".formatted(location), "Splash Potion of " + name);
        add("item.minecraft.lingering_potion.effect.%s".formatted(location), "Lingering Potion of " + name);
        add("item.gladius.coated_steel_shot.effect.%s".formatted(location), name + "-Coated Steel Shot");
    }

    protected void addSteelShot(Holder<Potion> key, String name) {
        String location = ResourceLocation.parse(key.getRegisteredName()).getPath();
        add("item.gladius.coated_steel_shot.effect.%s".formatted(location), name + "-Coated Steel Shot");
    }
}
