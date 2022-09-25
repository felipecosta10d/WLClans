package me.felipe.clans.org.listeners;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.managers.clan.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class PlayerListenersProvider implements Listener {

    private WLClansRegister wlClansRegister;
    private ClanManager clanManager;

    public PlayerListenersProvider(WLClansRegister wlClansRegister) {
        this.wlClansRegister = wlClansRegister;
        this.clanManager = wlClansRegister.getClanManager();
        Bukkit.getPluginManager().registerEvents(this, wlClansRegister.getPlugin());
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!clanManager.hasClanProfile(uuid)) {
            clanManager.createPlayerClanProfile(uuid);
        }
    }
}
