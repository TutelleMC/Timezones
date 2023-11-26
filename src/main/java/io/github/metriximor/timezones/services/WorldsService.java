package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.configs.WorldConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorldsService {
    private final List<WorldConfig> worldConfigs = new ArrayList<>();

    public void addWorldConfig(@NotNull final WorldConfig worldConfig) {
        worldConfigs.add(worldConfig);
    }

    @NotNull
    public List<WorldConfig> getAllWorldConfigs() {
        return worldConfigs;
    }
}
