package com.feliscape.gladius.data.datagen;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.datagen.advancement.GladiusAdvancementProvider;
import com.feliscape.gladius.data.datagen.lang.GladiusDeDeProvider;
import com.feliscape.gladius.data.datagen.lang.GladiusEnUsProvider;
import com.feliscape.gladius.data.datagen.loot.GladiusGlobalLootModifierProvider;
import com.feliscape.gladius.data.datagen.loot.GladiusLootTableProvider;
import com.feliscape.gladius.data.datagen.map.GladiusDataMapProvider;
import com.feliscape.gladius.data.datagen.model.GladiusBlockModelProvider;
import com.feliscape.gladius.data.datagen.model.GladiusItemModelProvider;
import com.feliscape.gladius.data.datagen.recipe.GladiusRecipeProvider;
import com.feliscape.gladius.data.datagen.tag.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        GladiusGeneratedEntries generatedEntries = new GladiusGeneratedEntries(packOutput, lookupProvider);
        lookupProvider = generatedEntries.getRegistryProvider();
        generator.addProvider(true, generatedEntries);

        generator.addProvider(true, new GladiusRecipeProvider(packOutput, lookupProvider));

        var blockTags = new GladiusBlockTagGenerator(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new GladiusItemTagGenerator(packOutput, lookupProvider, existingFileHelper, blockTags.contentsGetter()));
        generator.addProvider(true, new GladiusEntityTypeTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new GladiusMobEffectTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new GladiusDamageTypeTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new GladiusBiomeTagGenerator(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(true, new GladiusDataMapProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new GladiusAdvancementProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(), new GladiusLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new GladiusGlobalLootModifierProvider(packOutput, lookupProvider));

        generator.addProvider(true, new GladiusBlockModelProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new GladiusItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(true, new GladiusEnUsProvider(packOutput));
        generator.addProvider(true, new GladiusDeDeProvider(packOutput));
    }
}
