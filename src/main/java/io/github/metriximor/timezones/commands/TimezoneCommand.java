package io.github.metriximor.timezones.commands;

import io.github.metriximor.timezones.services.StateService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TimezoneCommand implements CommandExecutor, TabCompleter {
    private final String version;
    private final StateService stateService;

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

        switch (subCommand.get()) {
            case ENABLE -> {
                if (stateService.isEnabled()) {
                    sender.sendMessage("Timezones is already enabled!");
                    return true;
                }
                stateService.toggleState();
                return true;
            }
            case DISABLE -> {
                if (!stateService.isEnabled()) {
                    sender.sendMessage("Timezones is already disabled!");
                    return true;
                }
                stateService.toggleState();
                return true;
            }
            case VERSION -> {
                sender.sendMessage("Timezones %s by Metriximor".formatted(version));
                return true;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                @NotNull final Command command,
                                                @NotNull final String label,
                                                @NotNull final String[] args) {
        return Arrays.stream(SubCommand.values()).map(String::valueOf).map(String::toLowerCase).toList();
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
