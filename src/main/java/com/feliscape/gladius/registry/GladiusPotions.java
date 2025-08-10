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

//@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, Gladius.MOD_ID);

    /*public static final Holder<Potion> COMBUSTION = POTIONS.register("combustion", name -> new Potion(
            name.getPath(),
            new MobEffectInstance(GladiusMobEffects.COMBUSTION, 1)
    ));*/

    /*@SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event){
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(
                Potions.AWKWARD,
                Items.TNT,
                GladiusPotions.COMBUSTION
        );
    }*/

    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
