package me.felipe.clans.org.config;

import me.felipe.clans.org.WLClansRegister;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private Map<ConfigType, ConfigHandler> configurations;

    public ConfigManager() {
        configurations = new HashMap<>();
    }

    public void loadFiles(WLClansRegister wlClansRegister) {
        registerFile(ConfigType.CONFIG, new ConfigHandler(wlClansRegister.getPlugin(), "config"));
        registerFile(ConfigType.DATABASE, new ConfigHandler(wlClansRegister.getPlugin(), "database"));

        configurations.values().forEach(ConfigHandler::saveDefaultConfig);
    }

    public ConfigHandler getFile(ConfigType type) {
        return configurations.get(type);
    }

    public void reloadFiles() {
        configurations.values().forEach(ConfigHandler::reload);
    }

    public void registerFile(ConfigType type, ConfigHandler config) {
        configurations.put(type, config);
    }

    public FileConfiguration getFileConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
