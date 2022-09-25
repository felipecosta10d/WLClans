package me.felipe.clans.org.gui.listener;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.gui.utils.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GuiListeners implements Listener {

    private WLClansRegister wlClansRegister;

    public GuiListeners(WLClansRegister wlClansRegister) {
        this.wlClansRegister = wlClansRegister;
        wlClansRegister.getPlugin().getServer().getPluginManager().registerEvents(this, wlClansRegister.getPlugin());
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        InventoryHolder inventoryHolder = e.getInventory().getHolder();

        if (!(inventoryHolder instanceof Gui)) return;

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        Gui menu = (Gui) inventoryHolder;

        String item_name = item.getItemMeta().getDisplayName();
        if (item_name.equals("§cPágina anterior")) {
            if (menu.getPage() == 0) {
                e.setCancelled(true);
            } else {
                menu.setPage(menu.getPage() - 1);
                menu.open();
            }
        } else if (item_name.equals("§aPróxima página")) {
            if (!((menu.getIndex() + 1) >= 100)) {
                menu.setPage(menu.getPage() + 1);
                menu.open();
            } else {
                e.setCancelled(true);
            }
        } else if (item_name.equals("§cFechar")) {
            player.closeInventory();
        }

        menu.event(e);
    }
}
