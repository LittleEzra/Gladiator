package com.feliscape.gladius.registry.custom;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.team.Alliance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusRegistries {
    public static final Registry<Alliance> ALLIANCES = new RegistryBuilder<>(Keys.ALLIANCES)
            .sync(true)
            .create();

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event){
        event.register(ALLIANCES);
    }

    public static class Keys{

        public static final ResourceKey<Registry<Alliance>> ALLIANCES =
                ResourceKey.createRegistryKey(Gladius.location("alliances"));
    }
}
