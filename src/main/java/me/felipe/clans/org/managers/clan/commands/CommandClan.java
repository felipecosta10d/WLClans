package me.felipe.clans.org.managers.clan.commands;

import com.github.eokasta.commandlib.annotations.CommandInformation;
import com.github.eokasta.commandlib.providers.Command;
import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.gui.ClanGui;
import me.felipe.clans.org.managers.clan.commands.subcommands.SubCommandCreate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInformation(name = {"clan"}, onlyPlayer = true)
public class CommandClan extends Command {

    private WLClansRegister register;

    public CommandClan(WLClansRegister register) {
        this.register = register;
        registerSubCommand(new SubCommandCreate(register));
    }

    @Override
    public void perform(CommandSender commandSender, String s, String[] strings) {

        Player player = (Player) commandSender;

        if (strings.length == 0) {
            new ClanGui(player, register).open();
        }
    }
}
