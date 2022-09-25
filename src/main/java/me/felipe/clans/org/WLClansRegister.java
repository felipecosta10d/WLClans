package me.felipe.clans.org;

import com.github.eokasta.commandlib.CommandManager;
import lombok.Getter;
import me.felipe.clans.org.config.ConfigManager;
import me.felipe.clans.org.gui.listener.GuiListeners;
import me.felipe.clans.org.listeners.PlayerListenersProvider;
import me.felipe.clans.org.managers.clan.ClanManager;
import me.felipe.clans.org.managers.clan.commands.CommandClan;
import me.felipe.clans.org.managers.storage.StorageManager;
import me.felipe.clans.org.tasks.SaveTask;
import me.felipe.clans.org.utils.Settings;

public class WLClansRegister {

    @Getter
    private WLClans plugin;
    @Getter
    private ConfigManager configManager;
    @Getter
    private Settings settings;
    @Getter
    private StorageManager storageManager;
    @Getter
    private ClanManager clanManager;

    public WLClansRegister(WLClans plugin) {
        this.plugin = plugin;
        register();
    }

    public void register() {
        this.configManager = new ConfigManager();
        this.configManager.loadFiles(this);
        this.settings = new Settings(this);
        this.clanManager = new ClanManager(this);
        this.storageManager = new StorageManager(this);
        // LISTENERS
        new GuiListeners(this);
        new PlayerListenersProvider(this);
        // COMMANDS
        CommandManager commandManager = new CommandManager(getPlugin());
        commandManager.registerCommand(new CommandClan(this));

        // TASKS
        new SaveTask(this).runTaskTimerAsynchronously(getPlugin(), 20l * 600, 20l * 600);
    }

    public void unregister() {
        this.storageManager.save();
        this.storageManager.closeConnection();
    }
}
