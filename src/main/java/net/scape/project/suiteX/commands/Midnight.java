package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class Midnight extends BaseCommand {

    @Command(name = "midnight", permission = "suitex.command.midnight")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();

        String[] args = command.getArgs();

        if (args.length == 0) {
            player.getWorld().setTime(18000);
            String time_set_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("time_set");
            time_set_format = time_set_format.replace("{0}", "Midnight");
            time_set_format = addPlaceholders(time_set_format, player);
            msgPlayer(player, time_set_format);
        }
    }
}