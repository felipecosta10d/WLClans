package me.felipe.clans.org.gui.utils;

import lombok.Getter;
import lombok.Setter;
import me.felipe.clans.org.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Gui implements InventoryHolder {

    protected Player player;
    protected Inventory inventory;

    @Getter
    @Setter
    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public Gui(Player player) {
        this.player = player;
    }

    public abstract String name();

    public abstract int slots();

    public abstract void event(InventoryClickEvent e);

    public abstract void items();

    public void open() {
        inventory = Bukkit.createInventory(this, slots(), name());

        items();

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void border() {
        ItemStack item = new ItemFactory(Material.STAINED_GLASS_PANE).setData(2).setName(" ").build();

        int size = inventory.getSize();
        int rows = (size + 1) / 9;

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, item);
        }

        for (int i = size - 9; i < size; i++) {
            inventory.setItem(i, item);
        }

        for (int i = 2; i <= rows - 1; i++) {
            int[] slots = new int[]{i * 9 - 1, (i - 1) * 9};
            inventory.setItem(slots[0], item);
            inventory.setItem(slots[1], item);
        }

        if (getPage() > 0) {
            inventory.setItem((size - 6), new ItemFactory(Material.SKULL_ITEM).setData(3).setSkullTexture("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9").setName("§cPágina anterior").build());
            inventory.setItem((size - 4), new ItemFactory(Material.SKULL_ITEM).setData(3).setSkullTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf").setName("§aPróxima página").build());
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public int getPage() {
        return page;
    }

    public int getIndex() {
        return index;
    }

    public void addPage() {
        page += 1;
    }
}
