/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.wynntils.core.commands.CommandBase;
import com.wynntils.core.components.Models;
import com.wynntils.utils.StringUtils;
import com.wynntils.wynn.objects.profiles.ServerProfile;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ServerCommand extends CommandBase {
    private static final int UPDATE_TIME_OUT_MS = 3000;

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(getBaseCommandBuilder());

        dispatcher.register(Commands.literal("s").redirect(node));
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getBaseCommandBuilder() {
        LiteralCommandNode<CommandSourceStack> listNode = Commands.literal("list")
                .then(Commands.literal("up").executes(this::serverUptimeList))
                .executes(this::serverList)
                .build();

        LiteralCommandNode<CommandSourceStack> infoNode = Commands.literal("info")
                .then(Commands.argument("server", StringArgumentType.word()).executes(this::serverInfo))
                .executes(this::serverInfoHelp)
                .build();

        return Commands.literal("server")
                .then(listNode)
                .then(Commands.literal("ls").redirect(listNode))
                .then(Commands.literal("l").redirect(listNode))
                .then(infoNode)
                .then(Commands.literal("i").redirect(infoNode))
                .executes(this::serverHelp);
    }

    private int serverInfoHelp(CommandContext<CommandSourceStack> context) {
        context.getSource()
                .sendFailure(Component.literal("Usage: /s i,info <server> | Example: \"/s i WC1\"")
                        .withStyle(ChatFormatting.RED));
        return 1;
    }

    private int serverHelp(CommandContext<CommandSourceStack> context) {
        MutableComponent text = Component.literal(
                        """
                /s <command> [options]

                commands:
                l,ls,list (up) | list available servers
                i,info | get info about a server

                more detailed help:
                /s <command> help""")
                .withStyle(ChatFormatting.RED);

        context.getSource().sendSuccess(text, false);

        return 1;
    }

    private int serverInfo(CommandContext<CommandSourceStack> context) {
        if (!Models.ServerList.forceUpdate(UPDATE_TIME_OUT_MS)) {
            context.getSource()
                    .sendFailure(Component.literal("Network problems; using cached data")
                            .withStyle(ChatFormatting.RED));
        }

        String server = context.getArgument("server", String.class);

        if (server.startsWith("wc")) server = server.toUpperCase(Locale.ROOT);

        try {
            int serverNum = Integer.parseInt(server);
            server = "WC" + serverNum;
        } catch (Exception ignored) {
        }

        ServerProfile serverProfile = Models.ServerList.getServer(server);
        if (serverProfile == null) {
            context.getSource()
                    .sendFailure(Component.literal(server + " not found.").withStyle(ChatFormatting.RED));
            return 1;
        }

        Set<String> players = serverProfile.getPlayers();
        MutableComponent message = Component.literal(server + ":" + "\n")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal("Uptime: " + serverProfile.getUptime() + "\n")
                        .withStyle(ChatFormatting.DARK_AQUA))
                .append(Component.literal("Online players on " + server + ": " + players.size() + "\n")
                        .withStyle(ChatFormatting.DARK_AQUA));

        if (players.isEmpty()) {
            message.append(Component.literal("No players!").withStyle(ChatFormatting.AQUA));
        } else {
            message.append(Component.literal(String.join(", ", players)).withStyle(ChatFormatting.AQUA));
        }

        context.getSource().sendSuccess(message, false);

        return 1;
    }

    private int serverList(CommandContext<CommandSourceStack> context) {
        if (!Models.ServerList.forceUpdate(3000)) {
            context.getSource()
                    .sendFailure(Component.literal("Network problems; using cached data")
                            .withStyle(ChatFormatting.RED));
        }

        MutableComponent message = Component.literal("Server list:").withStyle(ChatFormatting.DARK_AQUA);

        for (String serverType : Models.ServerList.getWynnServerTypes()) {
            List<String> currentTypeServers = Models.ServerList.getServersSortedOnNameOfType(serverType);

            if (currentTypeServers.isEmpty()) continue;

            message.append("\n");

            message.append(Component.literal(
                            StringUtils.capitalizeFirst(serverType) + " (" + currentTypeServers.size() + "):\n")
                    .withStyle(ChatFormatting.GOLD));

            message.append(
                    Component.literal(String.join(", ", currentTypeServers)).withStyle(ChatFormatting.AQUA));
        }

        context.getSource().sendSuccess(message, false);

        return 1;
    }

    private int serverUptimeList(CommandContext<CommandSourceStack> context) {
        if (!Models.ServerList.forceUpdate(3000)) {
            context.getSource()
                    .sendFailure(Component.literal("Network problems; using cached data")
                            .withStyle(ChatFormatting.RED));
        }

        List<String> sortedServers = Models.ServerList.getServersSortedOnUptime();

        MutableComponent message = Component.literal("Server list:").withStyle(ChatFormatting.DARK_AQUA);
        for (String server : sortedServers) {
            message.append("\n");
            message.append(Component.literal(
                            server + ": " + Models.ServerList.getServer(server).getUptime())
                    .withStyle(ChatFormatting.AQUA));
        }

        context.getSource().sendSuccess(message, false);

        return 1;
    }
}
