package com.feliscape.gladius.content.command;

import com.feliscape.gladius.Gladius;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        AllianceCommand.register(event.getDispatcher());
    }
}
