package com.feliscape.gladius.content.command;

import com.feliscape.gladius.content.entity.team.Alliance;
import com.feliscape.gladius.registry.custom.GladiusRegistries;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class AllianceArgument implements ArgumentType<String> {
    private static final DynamicCommandExceptionType ERROR_TEAM_NOT_FOUND = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("team.notFound", o)
    );

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    public static AllianceArgument alliance() {
        return new AllianceArgument();
    }

    private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> context, ResourceKey<? extends Registry<T>> registryKey) {
        return ((CommandSourceStack)context.getSource()).getServer().registryAccess().registryOrThrow(registryKey);
    }

    public static Alliance getAlliance(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        String s = context.getArgument(name, String.class);
        try{

            var registry = getRegistry(context, GladiusRegistries.Keys.ALLIANCES);
            var resourceLocation = ResourceLocation.parse(s);
            return registry.get(resourceLocation);
        } catch (Exception e){
            throw ERROR_TEAM_NOT_FOUND.create(s);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider sharedSuggestionProvider){
            var registry = sharedSuggestionProvider.registryAccess().registry(GladiusRegistries.Keys.ALLIANCES);
            if (registry.isPresent())
                return SharedSuggestionProvider.suggest(registry.get().holders().map(v -> v.key().location().toString()), builder);
        }
        return Suggestions.empty();
    }
}
