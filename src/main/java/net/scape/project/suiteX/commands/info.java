package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.handlers.xPlayer;
import net.scape.project.suiteX.handlers.xPlayerData;
import net.scape.project.suiteX.utils.Utils;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class info extends BaseCommand {

    @Command(name = "info", permission = "suitex.command.info")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            sendUsage(player);
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore()) {
                Utils.msgPlayer(player, "&cThis player has never played.");
                return;
            }

            xPlayer data = SuiteX.getInstance().getPlayerManager().get(target.getUniqueId());

            if (data == null) {
                Utils.msgPlayer(player, "&cNo data to retrieve.");
                return;
            }

            Utils.msgPlayer(player, "&e" + target.getName() + "'s data:",
                    "&7Displayname: " + data.displayname(),
                    "&7NickName: " + data.nickname(),
                    "&7Balance: " + data.money(),
                    "&7Bank: " + data.bank(),
                    "&7AFK: " + data.isAFK(),
                    "&7Muted: " + data.isMuted(),
                    "&7Last Seen: " + data.lastSeen());

        }
    }

    private void sendUsage(CommandSender sender) {
        Utils.msgPlayer(sender, "&cUsage: /info <player>");
    }
}
