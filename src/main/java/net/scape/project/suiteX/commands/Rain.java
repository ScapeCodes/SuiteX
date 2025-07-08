package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class Rain extends BaseCommand {

    @Command(name = "rain", permission = "suitex.command.rain")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();

        String[] args = command.getArgs();

        if (args.length == 0) {
            player.getWorld().setStorm(true);

            String weather_set_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("weather_set");
            weather_set_format = weather_set_format.replace("{0}", "Rain");
            weather_set_format = addPlaceholders(weather_set_format, player);
            msgPlayer(player, weather_set_format);
        }
    }
}