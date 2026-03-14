package com.feliscape.gladius.content.command;

import com.feliscape.gladius.Gladius;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        AllianceCommand.register(event.getDispatcher());
    }

    public static class ArgumentTypes{
        public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES =
                DeferredRegister.create(
                        Registries.COMMAND_ARGUMENT_TYPE, Gladius.MOD_ID
                );

        public static final Supplier<ArgumentTypeInfo<AllianceArgument,SingletonArgumentInfo<AllianceArgument>.Template>>
                ALLIANCE = register("alliance", AllianceArgument.class, SingletonArgumentInfo.contextFree(AllianceArgument::new));

        private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
        Supplier<ArgumentTypeInfo<A, T>> register(String name, Class<A> infoClass, I argumentTypeInfo){
            ArgumentTypeInfos.registerByClass(infoClass, argumentTypeInfo);
            return ARGUMENT_TYPES.register(name,
                    () -> argumentTypeInfo);
        }

        public static void register(IEventBus eventBus){
            ARGUMENT_TYPES.register(eventBus);
        }
    }
}
