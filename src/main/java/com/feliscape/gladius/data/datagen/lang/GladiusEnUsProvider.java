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
        this.addItem(GladiusItems.BLAZING_HEART, "Blazing Heart");

        this.addItem(GladiusItems.EXPLOSIVE_ARROW, "Explosive Arrow");
        this.add("item.nuanced_combat.explosive_arrow.power", "Power: %1$s");
        this.addItem(GladiusItems.PRISMARINE_ARROW, "Prismarine Arrow");
        this.addItem(GladiusItems.WINGED_ARROW, "Winged Arrow");

        this.addItem(GladiusItems.OIL_BOTTLE, "Oil Bottle");
        this.addItem(GladiusItems.FIREBRAND, "Firebrand");

        this.addItem(GladiusItems.FLASH_POWDER, "Flash Powder");
        this.addItem(GladiusItems.CRYSTAL_BUTTERFLY, "Crystal Butterfly");

        this.addItem(GladiusItems.GILDED_DAGGER, "Gilded Dagger");
        this.addItem(GladiusItems.CLAYMORE, "Claymore");
        this.addItem(GladiusItems.FLAMBERGE, "Flamberge");
        this.add("item.gladius.tooltip.two_handed", "Two-Handed");

        this.addEntityType(GladiusEntityTypes.EXPLOSIVE_ARROW, "Explosive Arrow");
        this.addEntityType(GladiusEntityTypes.PRISMARINE_ARROW, "Prismarine Arrow");
        this.addEntityType(GladiusEntityTypes.WINGED_ARROW, "Winged Arrow");
        this.addEntityType(GladiusEntityTypes.OIL_BOTTLE, "Oil Bottle");
        this.addEntityType(GladiusEntityTypes.FIREBRAND, "Firebrand");

        this.addEntityType(GladiusEntityTypes.FLASH_POWDER_CLOUD, "Flash Powder Cloud");
        this.addEntityType(GladiusEntityTypes.CRYSTAL_BUTTERFLY, "Crystal Butterfly");

        this.addMobEffect(GladiusMobEffects.STUN, "Stun");
        this.addMobEffect(GladiusMobEffects.OVERBURDENED, "Overburdened");
        this.addMobEffect(GladiusMobEffects.BLEEDING, "Bleeding");
        this.addMobEffect(GladiusMobEffects.FLAMMABLE, "Flammable");
        this.addMobEffect(GladiusMobEffects.FLASHED, "Flashed");

        this.addEnchantment(GladiusEnchantments.STUNNING, "Stunning");

        this.addSubtitle(GladiusSoundEvents.CLAYMORE_BLOCK, "Claymore blocks");
        this.addSubtitle(GladiusSoundEvents.FIREBRAND_LIGHT, "Firebrand lights fire");
        this.addSubtitle(GladiusSoundEvents.FLASH_POWDER_CRACKLE, "Flash Powder crackles");

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

        this.addConfigSection("effects", "Effects");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.stunTrailResolution, "Stun Trail Resolution");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.showBlood, "Show Blood");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.extraOil, "Extra Oil");
        this.addConfigSection("flash_powder", "Flash Powder");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowderFlashing, "Flash Powder Flashing");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.flashPowderLightChance, "Flash Powder Light Chance");
        this.addConfigValue("client", GladiusClientConfig.CONFIG.darkFlash, "Dark Flash");

        this.addConfigSection("entities", "Entities");
        this.addConfigValue("server", GladiusServerConfig.CONFIG.crystalButterflySearchRange, "Crystal Butterfly Search Range");
        this.addConfigSection("projectiles", "Projectiles");
        this.addConfigValue("server", GladiusServerConfig.CONFIG.firebrandMakesFire, "Firebrand Makes Fire");
    }

    protected void addPotion(Holder<Potion> key, String name) {
        String location = ResourceLocation.parse(key.getRegisteredName()).getPath();
        add("item.minecraft.tipped_arrow.effect.%s".formatted(location), "Arrow of " + name);
        add("item.minecraft.potion.effect.%s".formatted(location), "Potion of " + name);
        add("item.minecraft.splash_potion.effect.%s".formatted(location), "Splash Potion of " + name);
        add("item.minecraft.lingering_potion.effect.%s".formatted(location), "Lingering Potion of " + name);
    }
}
