package me.felipe.clans.org.gui;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.gui.utils.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClanMembersGui extends Gui {

    private WLClansRegister wlClansRegister;

    public ClanMembersGui(Player player, WLClansRegister wlClansRegister) {
        super(player);
        this.wlClansRegister = wlClansRegister;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public int slots() {
        return 0;
    }

    @Override
    public void event(InventoryClickEvent e) {

    }

    @Override
    public void items() {

    }
}
