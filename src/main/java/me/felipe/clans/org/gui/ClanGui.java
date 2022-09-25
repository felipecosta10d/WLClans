package me.felipe.clans.org.gui;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.gui.utils.Gui;
import me.felipe.clans.org.managers.clan.Clan;
import me.felipe.clans.org.managers.clan.ClanManager;
import me.felipe.clans.org.managers.clan.decoration.LettersBanner;
import me.felipe.clans.org.managers.clan.profile.ClanPlayerProfile;
import me.felipe.clans.org.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ClanGui extends Gui {

    private WLClansRegister wlClansRegister;
    private ClanManager clanManager;
    private UUID playerUuid;

    public ClanGui(Player player, WLClansRegister wlClansRegister) {
        super(player);
        this.wlClansRegister = wlClansRegister;
        this.clanManager = wlClansRegister.getClanManager();
        this.playerUuid = player.getUniqueId();
    }

    @Override
    public String name() {
        if (!clanManager.playerHasClan(playerUuid)) {
            return "§7Criar clan";
        } else {
            return "§7" + clanManager.getPlayerClan(playerUuid).getName() + " - " + clanManager.getPlayerClan(playerUuid).getTag();
        }
    }

    @Override
    public int slots() {
        if (!clanManager.playerHasClan(playerUuid)) {
            return 27;
        } else {
            return 45;
        }
    }

    @Override
    public void event(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        String inventoryName = e.getInventory().getName();
        if (inventoryName.equals(name())) {
            String itemName = item.getItemMeta().getDisplayName();
            switch (itemName) {
                case "§eCriar um clan!. ":
                    Bukkit.dispatchCommand(player, "clan criar");
                    player.closeInventory();
                    break;
                case "§eTeleportar para a base do clan":

                    break;
            }
        }
    }

    @Override
    public void items() {
        if (!wlClansRegister.getClanManager().playerHasClan(playerUuid)) {
            inventory.setItem(11, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture(player)
                    .setName("§eSeus convites")
                    .addLore("§7Clique para visualizar seus convites para clans!. ")
                    .build());
            inventory.setItem(13, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture("29fda3093c073e4483d90c0bf372094511e51180ed51bcc9dadc2fde52f4db39")
                    .setName("§eCriar um clan!. ")
                    .build());
            inventory.setItem(15, new ItemFactory(Material.ARMOR_STAND)
                    .setName("§eMelhores clans")
                    .addLore("§7Clique para verificar os melhores clans do servidor!. ")
                    .build());
        } else {
            Clan clan = clanManager.getPlayerClan(playerUuid);
            inventory.setItem(3, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture(LettersBanner.valueOf(Character.toString(clan.getTag().charAt(0)).toUpperCase()).getValue())
                    .setName(" ")
                    .build());
            inventory.setItem(4, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture(LettersBanner.valueOf(Character.toString(clan.getTag().charAt(1)).toUpperCase()).getValue())
                    .setName(" ")
                    .build());
            inventory.setItem(5, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture(LettersBanner.valueOf(Character.toString(clan.getTag().charAt(2)).toUpperCase()).getValue())
                    .setName(" ")
                    .build());
            ClanPlayerProfile profile = wlClansRegister.getClanManager().getClanProfile(playerUuid);
            inventory.setItem(19, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture(player)
                    .setName("§bSeu perfil")
                    .addLore("",
                            " §fSeu cargo: §6" + profile.getClanRank().name() + " ",
                            "",
                            " §fMatou: §a" + profile.getKills() + " ",
                            " §fMorreu: §c" + profile.getDeaths() + " ",
                            " §fKDR: §e" + profile.getKdr() + " ",
                            "")
                    .build());
            inventory.setItem(28, new ItemFactory(Material.SKULL_ITEM)
                    .setData(3)
                    .setSkullTexture("cd531c9a01120becb8c63a388f7be5d1e088c7afd57c3ba652f54f71b166e65b")
                    .setName("§bMembros do clan")
                            .addLore("§7Clique para visualizar os membros do clan!.")
                    .build());
            inventory.setItem(34, new ItemFactory(Material.DARK_OAK_DOOR_ITEM)
                    .setName("§eTeleportar para a base do clan")
                    .addLore(clan.baseIsSet() ? "§7Clique para teleportar!. " : "§cA base do clan ainda não foi setada!. ")
                    .build());
        }
    }
}
