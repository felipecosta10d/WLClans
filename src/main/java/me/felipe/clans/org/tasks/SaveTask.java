package me.felipe.clans.org.tasks;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.managers.storage.StorageManager;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {

    private StorageManager storageManager;

    public SaveTask(WLClansRegister wlClansRegister) {
        this.storageManager = wlClansRegister.getStorageManager();
    }

    @Override
    public void run() {
    }
}
