package me.felipe.clans.org;

import org.bukkit.plugin.java.JavaPlugin;

public class WLClans extends JavaPlugin {

    private WLClansRegister wlClansRegister;

    @Override
    public void onEnable() {
        this.wlClansRegister = new WLClansRegister(this);
    }

    @Override
    public void onDisable() {
        this.wlClansRegister.unregister();
    }
}
