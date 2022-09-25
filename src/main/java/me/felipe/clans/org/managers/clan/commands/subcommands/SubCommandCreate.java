package me.felipe.clans.org.managers.clan.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.SubCommand;
import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.managers.clan.ClanManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@SubCommandInformation(name = {"create", "criar"})
public class SubCommandCreate extends SubCommand {

    private WLClansRegister register;

    public SubCommandCreate(WLClansRegister register) {
        this.register = register;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) throws CommandLibException {
        if (!(commandSender instanceof Player)) return;

        Player player = (Player) commandSender;
        UUID playerUUID = player.getUniqueId();
        if (strings.length < 2) {
            player.sendMessage("§cUtilize: '/clan criar (tag) (nome do clan)'. ");
            return;
        }
        ClanManager clanManager = register.getClanManager();
        if (clanManager.playerHasClan(playerUUID)) {
            player.sendMessage("§cVocê já possui um clan!. ");
            return;
        }
        String tag = strings[0].toUpperCase();
        if (tag.length() != 3) {
            player.sendMessage("§cA tag do clan só pode conter 3 caracteres, ex: 'TST'. ");
            return;
        }
        if (clanManager.existClanWithTag(tag)) {
            player.sendMessage("§cJá existe um clan utilizando essa tag!. ");
            return;
        }
        String name = strings[1];
        if (name.length() > 16 || name.length() < 5) {
            player.sendMessage("§cO número de caracteres no nome do clan não pode ser menor que 5 e nem maior que 16!. ");
            return;
        }
        if (clanManager.existClanWithName(name)) {
            player.sendMessage("§cJá existe um clan utilizando esse nome!. ");
            return;
        }
        clanManager.createClan(name, tag, playerUUID);
        player.sendMessage("§aVocê criou um clan com o nome §f" + name + " (" + tag + ")§a!. ");
    }
}
