package com.feliscape.gladius.content.command;

import com.feliscape.gladius.content.attachment.AllianceData;
import com.feliscape.gladius.content.entity.team.Alliance;
import com.feliscape.gladius.registry.custom.GladiusRegistries;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class AllianceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralCommandNode<CommandSourceStack> literalcommandnode = dispatcher.register(
                Commands.literal("alliance")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.literal("remove")
                                        .executes(context -> remove(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "targets")
                                        ))
                                )
                                .then(Commands.literal("set")
                                        .then(Commands.argument("id", AllianceArgument.alliance())
                                                .executes(
                                                        context -> set(
                                                                context.getSource(),
                                                                EntityArgument.getEntities(context, "targets"),
                                                                AllianceArgument.getAlliance(context, "id")
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static int remove(CommandSourceStack source, Collection<? extends Entity> targets) {
        var validEntities = targets.stream().filter(e -> e instanceof LivingEntity && e.hasData(AllianceData.type())).collect(Collectors.toSet());
        if (validEntities.isEmpty()){
            source.sendFailure(
                    Component.translatable("commands.gladius.alliance.remove.failure.no_valid_entities")
            );
            return 0;
        }

        for (Entity entity : validEntities){
            entity.removeData(AllianceData.type());
        }


        if (targets.size() == 1) {
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.gladius.alliance.remove.success.single", targets.iterator().next().getDisplayName()
                    ),
                    true
            );
        } else {
            source.sendSuccess(
                    () -> Component.translatable("commands.gladius.alliance.remove.success.multiple", targets.size()), true
            );
        }

        return targets.size();
    }
    private static int set(CommandSourceStack source, Collection<? extends Entity> targets, Alliance alliance) {
        var validEntities = targets.stream().filter(e -> e instanceof LivingEntity).collect(Collectors.toSet());
        if (validEntities.isEmpty()){
            source.sendFailure(
                    Component.translatable("commands.gladius.alliance.set.failure.no_valid_entities")
            );
            return 0;
        }


        for (Entity entity : targets) {
            entity.setData(AllianceData.type(), alliance);
        }
        var registry = source.registryAccess().registryOrThrow(GladiusRegistries.Keys.ALLIANCES);
        var id = registry.getKey(alliance);

        if (targets.size() == 1) {
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.gladius.alliance.set.success.single", targets.iterator().next().getDisplayName(),
                            id == null ? "unknown alliance" : id.toString()
                    ),
                    true
            );
        } else {
            source.sendSuccess(
                    () -> Component.translatable("commands.gladius.alliance.set.success.multiple", targets.size(),
                            id == null ? "unknown alliance" :  id.toString()), true
            );
        }

        return targets.size();
    }
}
