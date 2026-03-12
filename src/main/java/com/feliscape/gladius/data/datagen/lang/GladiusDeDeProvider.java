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

@SuppressWarnings("SpellCheckingInspection")
public class GladiusDeDeProvider extends GladiusLanguageProvider{
    public GladiusDeDeProvider(PackOutput output) {
        super(output, "de_de");
    }

    @Override
    protected void addTranslations() {
        this.add("item.gladius.wip", "WIP - Im Überlebensmodus nicht erhältlich");

        this.addBlock(GladiusBlocks.FRIGID_ICE, "Frostiges Eis");
        this.addBlock(GladiusBlocks.MIST_TRAP, "Nebelfalle");
        this.addBlock(GladiusBlocks.FLAME_TRAP, "Flammenfalle");

        this.addBlock(GladiusBlocks.BLACKSTONE_GOLEM_HEART, "Schwarzsteingolemherz");

        this.addItem(GladiusItems.FROSTMANCER_SPAWN_EGG, "Frostmagier-Spawn-Ei");
        this.addItem(GladiusItems.BLACKSTONE_GOLEM_SPAWN_EGG, "Schwarzsteingolem-Spawn-Ei");
        this.addItem(GladiusItems.PIGLIN_SHAMAN_SPAWN_EGG, "Piglin-Schamanen-Spawn-Ei");
        this.addItem(GladiusItems.PIGLIN_BOMBER_SPAWN_EGG, "Piglin-Bomber-Spawn-Ei");
        this.addItem(GladiusItems.PIGLIN_WARLORD_SPAWN_EGG, "Piglin-Kriegsherr-Spawn-Ei");

        this.addItem(GladiusItems.BLAZING_HEART, "Loderndes Herz");
        this.addItem(GladiusItems.FRIGID_SEED, "Frostiger Samen");
        this.addItemTooltip(GladiusItems.FRIGID_SEED, "Kann in Blaueis gepflanzt werden");
        this.addItem(GladiusItems.FRIGID_SHARD, "Frostige Scherbe");
        this.addItem(GladiusItems.HEARTH_STONE, "Herdstein");

        this.addItem(GladiusItems.EXPLOSIVE_ARROW, "Explosiver Pfeil");
        this.add("item.nuanced_combat.explosive_arrow.power", "Kraft: %1$s");
        this.addItem(GladiusItems.PRISMARINE_ARROW, "Prismarinpfeil");
        this.addItem(GladiusItems.WINGED_ARROW, "Geflügelter Pfeil");

        this.addItem(GladiusItems.OIL_BOTTLE, "Ölflasche");
        this.addItem(GladiusItems.FIREBRAND, "Feuerstock");
        this.addItem(GladiusItems.ICE_BOMB, "Eisbombe");
        this.addItem(GladiusItems.BOMB, "Bombe");

        this.addItem(GladiusItems.FLASH_POWDER, "Blitzpulver");
        this.addItem(GladiusItems.CRYSTAL_BUTTERFLY, "Kristallschmetterling");

        this.addItem(GladiusItems.GILDED_DAGGER, "Vergoldeter Dolch");
        this.addItem(GladiusItems.CLAYMORE, "Claymore");
        this.addItem(GladiusItems.FLAMBERGE, "Flammenschwert");
        this.addItem(GladiusItems.GOLDEN_SCIMITAR, "Goldenes Säbel");
        this.addItem(GladiusItems.GOLDEN_WAND, "Goldener Zauberstab");
        this.addItem(GladiusItems.FROZEN_WAND, "Gefrorener Zauberstab");
        this.addItem(GladiusItems.TORRID_STANDARD, "Flammende Standarte");
        this.addItem(GladiusItems.HOGLIN_TUSK, "Hoglin Tusk");
        this.addItem(GladiusItems.FLAMEWALKERS, "Flammenläufer");
        this.addItem(GladiusItems.NIGHTWALKER_HOOD, "Nachtläufer Kapuze");
        this.addItem(GladiusItems.POWER_GAUNTLETS, "Macht-Handschuhe");
        this.addItem(GladiusItems.LEATHER_GAUNTLETS, "Lederhandschuhe");
        this.add("item.gladius.tooltip.two_handed", "Beidhändig");

        this.addInstrument(GladiusInstruments.HOGLIN_TUSK, "Kampfschrei");

        this.addAspect(GladiusAspects.FIRE, "🔥 Feuer");
        this.addAspect(GladiusAspects.MAGIC, "🧪 Magie");
        this.addAspect(GladiusAspects.ICE, "❄ Eis");
        this.addAspect(GladiusAspects.POISON, "☠ Gift");

        this.addEntityType(GladiusEntityTypes.MAGIC_ORB, "Magische Kugel");
        this.addEntityType(GladiusEntityTypes.EXPLOSIVE_ARROW, "Explosiver Pfeil");
        this.addEntityType(GladiusEntityTypes.PRISMARINE_ARROW, "Prismarinpfeil");
        this.addEntityType(GladiusEntityTypes.WINGED_ARROW, "Geflügelter Pfeil");
        this.addEntityType(GladiusEntityTypes.OIL_BOTTLE, "Ölflasche");
        this.addEntityType(GladiusEntityTypes.FIREBRAND, "Feuerstock");

        this.addEntityType(GladiusEntityTypes.MAGMA_POOL, "Lavasee");
        this.addEntityType(GladiusEntityTypes.FIRE_WAKE, "Feuerwake");
        this.addEntityType(GladiusEntityTypes.TORRID_WISP, "Brennendes Licht");

        this.addEntityType(GladiusEntityTypes.ICE_BOMB, "Eisbombe");
        this.addEntityType(GladiusEntityTypes.ICE_SPIKE_SPAWNER, "Eisstachel-Spawner");
        this.addEntityType(GladiusEntityTypes.ICE_SPIKE, "Eisstachel");
        this.addEntityType(GladiusEntityTypes.ICE_BLOCK, "Eisblock");
        this.addEntityType(GladiusEntityTypes.FROSTMANCER, "Frostmagier");
        this.addEntityType(GladiusEntityTypes.BLACKSTONE_GOLEM, "Schwarzsteingolem");
        this.addEntityType(GladiusEntityTypes.PIGLIN_SHAMAN, "Piglin-Schamane");
        this.addEntityType(GladiusEntityTypes.PIGLIN_BOMBER, "Piglin-Bomber");
        this.addEntityType(GladiusEntityTypes.PIGLIN_WARLORD, "Piglin-Kriegsherr");

        this.addEntityType(GladiusEntityTypes.FLASH_POWDER_CLOUD, "Blitzpulverwolke");
        this.addEntityType(GladiusEntityTypes.CRYSTAL_BUTTERFLY, "Kristallschmetterling");

        this.addMobEffect(GladiusMobEffects.STUN, "Betäubung");
        this.addMobEffect(GladiusMobEffects.OVERBURDENED, "Überlastet");
        this.addMobEffect(GladiusMobEffects.BLEEDING, "Bluten");
        this.addMobEffect(GladiusMobEffects.FLAMMABLE, "Brennbar");
        this.addMobEffect(GladiusMobEffects.FLASHED, "Geblitzt");
        this.addMobEffect(GladiusMobEffects.FREEZING, "Frieren");
        this.addMobEffect(GladiusMobEffects.FROST_RESISTANCE, "Frostresistenz");
        this.addMobEffect(GladiusMobEffects.STABILITY, "Stabilität");

        this.addEnchantment(GladiusEnchantments.STUNNING, "Betäubung");

        this.addSubtitle(GladiusSoundEvents.CLAYMORE_BLOCK, "Claymore pariert");
        this.addSubtitle(GladiusSoundEvents.GILDED_DAGGER_STAB, "Vergoldeter Dolch sticht");
        this.addSubtitle(GladiusSoundEvents.WINGED_ARROW_HIT, "Geflügelter Pfeil trifft");
        this.addSubtitle(GladiusSoundEvents.FIREBRAND_LIGHT, "Feuerstock entzündet Feuer");
        this.addSubtitle(GladiusSoundEvents.FIRE_WAKE_ERUPT, "Feuerwake bricht aus");
        this.addSubtitle(GladiusSoundEvents.FLASH_POWDER_CRACKLE, "Blitzpulver knistert");
        this.addSubtitle(GladiusSoundEvents.HEARTH_STONE_USE, "Herdstein entzündet");

        this.addSubtitle(GladiusSoundEvents.ICE_BOMB_THROW, "Eisbombe fliegt");
        this.addSubtitle(GladiusSoundEvents.ICE_BOMB_SHATTER, "Eisbombe explodiert");
        this.addSubtitle(GladiusSoundEvents.ICE_SPIKE_RISE, "Eisstachel steigt");
        this.addSubtitle(GladiusSoundEvents.ICE_BLOCK_SHATTER, "Eisblock zerbricht");

        this.addSubtitle(GladiusSoundEvents.SPELL, "Zauber ist benutzt worden");

        this.addSubtitle(GladiusSoundEvents.FROSTMANCER_SHIELD_BREAK, "Frostmagiers Schild zerbricht");
        this.addSubtitle(GladiusSoundEvents.TORRID_WISP_SPAWN, "Brennendes Licht flammt auf");

        this.addSubtitle(GladiusSoundEvents.FRIGID_ICE_FREEZE, "Frostiges Eis breitet sich aus");
        this.addSubtitle(GladiusSoundEvents.FLAME_TRAP_IGNITE, "Flammenfalle entzündet");
        this.addSubtitle(GladiusSoundEvents.FLAME_TRAP_BURN, "Flammenfalle brennt");
        this.addSubtitle(GladiusSoundEvents.FLAME_TRAP_STOP, "Flammenfalle brennt aus");
        this.addSubtitle(GladiusSoundEvents.MIST_TRAP_BREATH, "Nebelfalle atmet");


        this.addDeathMessage(GladiusDamageTypes.BLEEDING, "%1$s verblutete");

        this.addDeathMessage(GladiusDamageTypes.MAGIC_PROJECTILE, "%1$s wurde von %2$s erschossen");
        this.addDeathMessageItem(GladiusDamageTypes.MAGIC_PROJECTILE, "%1$s wurde von %2$s mit %3$s erschossen");

        this.addDeathMessage(GladiusDamageTypes.INDIRECT_SKEWERING, "%1$s wurde von %2$s aufgespießt");
        this.addDeathMessageItem(GladiusDamageTypes.INDIRECT_SKEWERING, "%1$s wurde von %2$s mit %3$s aufgespießt");
        this.addDeathMessage(GladiusDamageTypes.SKEWERING, "%1$s wurde aufgespießt");
        this.addDeathMessagePlayer(GladiusDamageTypes.SKEWERING, "%1$s wurde beim Versuch, %2$s zu entkommen, aufgespießt");

        this.addDeathMessagePlayer(GladiusDamageTypes.TORRID_WISP, "%1$s wurde von %2$s verbrannt");
        this.addDeathMessage(GladiusDamageTypes.TORRID_WISP, "%1$s wurde verbrannt");
        this.addDeathMessagePlayer(GladiusDamageTypes.BLACKSTONE_GOLEM_CHARGING, "%1$s wurde von %2$s zerquetscht");
        this.addDeathMessage(GladiusDamageTypes.BLACKSTONE_GOLEM_CHARGING, "%1$s wurde zerquetscht");

        this.addPotion(GladiusPotions.FROST_RESISTANCE, "Trank der Frostresistenz");
        this.addSplashPotion(GladiusPotions.FROST_RESISTANCE, "Wurftrank der Frostresistenz");
        this.addLingeringPotion(GladiusPotions.FROST_RESISTANCE, "Verweiltrank der Frostresistenz");
        this.addTippedArrow(GladiusPotions.FROST_RESISTANCE, "Pfeil der Frostresistenz");

        this.addPotion(GladiusPotions.STABILITY, "Trank der Stabilität");
        this.addSplashPotion(GladiusPotions.STABILITY, "Wurftrank der Stabilität");
        this.addLingeringPotion(GladiusPotions.STABILITY, "Verweiltrank der Stabilität");
        this.addTippedArrow(GladiusPotions.STABILITY, "Pfeil der Stabilität");

        this.addAttribute(GladiusAttributes.USING_SPEED_MODIFIER, "Geschwindigkeit bei Verwendung");

        this.addAdvancement(GladiusAdvancements.FIREBRAND,
                "Pyromane",
                "Werfe einen Feuerstock ohne jeglicher Rücksicht auf deine Umgebung");
        this.addAdvancement(GladiusAdvancements.OIL_BOTTLE,
                "Küchenalpträume",
                "Erhalte eine Ölflasche von einem Dorfbewohner");
        this.addAdvancement(GladiusAdvancements.POCKET_SAND,
                "Taschen-Sand!",
                "Werfe Blitzpulver in die Luft, um deine Feinde abzulenken");
    }

    /*
        item.minecraft.tipped_arrow.effect.{potion}:     Pfeil der ...
        item.minecraft.potion.effect.{potion}:           Trank der ...
        item.minecraft.splash_potion.effect.{potion}:    Wurftrank der ...
        item.minecraft.lingering_potion.effect.{potion}: Verweiltrank der ...
     */
}
