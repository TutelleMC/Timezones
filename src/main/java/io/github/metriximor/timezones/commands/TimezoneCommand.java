package io.github.metriximor.timezones.commands;

import io.github.metriximor.timezones.TimezonesPlugin;
import io.github.metriximor.timezones.services.RegionService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class TimezoneCommand implements CommandExecutor {
    private final String version;
    private final RegionService regionService;
    private final Logger logger;
    private boolean isEnabled = true;

    private enum SubCommand {
        ENABLE,
        DISABLE,
        VERSION
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender,
                             @NotNull final Command command,
                             @NotNull final String label,
                             @NotNull final String[] args) {
        if (args.length == 0) {
            return false;
        }

        var subCommand = parseArg(args[0]);
        if (subCommand.isEmpty()) {
            return false;
        }

        final var world = Bukkit.getWorld("world");
        if (world == null) {
            logger.warning("Couldn't find world!");
            return false;
        }

        final var worldConfig = TimezonesPlugin.getWorldConfig();
        switch (subCommand.get()) {
            case ENABLE -> {
                if (isEnabled) {
                    sender.sendMessage("Timezones is already enabled!");
                    return true;
                }
                isEnabled = true;

                sender.sendMessage("Enabling Timezones for world %s".formatted(world));
                regionService.registerRegions(world, worldConfig);
                return true;
            }
            case DISABLE -> {
                if (!isEnabled) {
                    sender.sendMessage("Timezones is already disabled!");
                    return true;
                }
                isEnabled = false;

                sender.sendMessage("Disabling timezones for world %s".formatted(world));
                regionService.unregisterRegions(world);
                return true;
            }
            case VERSION -> {
                sender.sendMessage("Timezones %s by Metriximor".formatted(version));
                return true;
            }
        }

        return false;
    }

    @NotNull
    private Optional<SubCommand> parseArg(@NotNull final String arg) {
        try {
            return Optional.of(SubCommand.valueOf(arg.toUpperCase()));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
