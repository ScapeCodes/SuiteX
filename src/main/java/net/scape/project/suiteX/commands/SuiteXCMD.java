package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.Utils;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class SuiteXCMD extends BaseCommand {

    @Command(name = "suitex", aliases = "sx", permission = "suitex.admin")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sendUsage(player);
            return;
        }

        if (args[0].toLowerCase().equals("reload")) {
            SuiteX.getInstance().reloadCore();

            String r_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("reload");
            r_format = addPlaceholders(r_format, player);
            msgPlayer(player, r_format);
        } else {
            sendUsage(player);
        }
    }

    private void sendUsage(CommandSender sender) {
        msgPlayer(sender, "&cUsage: /sx <args>");
    }
}