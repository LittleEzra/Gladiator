package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, Gladius.MOD_ID);

    public static final Holder<Potion> FROST_RESISTANCE = POTIONS.register("frost_resistance", name -> new Potion(
            "frost_resistance", new MobEffectInstance(GladiusMobEffects.FROST_RESISTANCE, 3600)
    ));
    public static final Holder<Potion> LONG_FROST_RESISTANCE = POTIONS.register("long_frost_resistance", name -> new Potion(
            "frost_resistance", new MobEffectInstance(GladiusMobEffects.FROST_RESISTANCE, 9600)
    ));
    public static final Holder<Potion> STABILITY = POTIONS.register("stability", name -> new Potion(
            "stability", new MobEffectInstance(GladiusMobEffects.STABILITY, 9600)
    ));
    public static final Holder<Potion> LONG_STABILITY = POTIONS.register("long_stability", name -> new Potion(
            "stability", new MobEffectInstance(GladiusMobEffects.STABILITY, 19200)
    ));

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event){
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addStartMix(Items.NETHER_WART_BLOCK, GladiusPotions.STABILITY);
        builder.addMix(GladiusPotions.STABILITY, Items.REDSTONE, GladiusPotions.LONG_STABILITY);

        builder.addStartMix(GladiusItems.FRIGID_SHARD.get(), GladiusPotions.FROST_RESISTANCE);
        builder.addMix(GladiusPotions.FROST_RESISTANCE, Items.REDSTONE, GladiusPotions.LONG_FROST_RESISTANCE);
    }

    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
