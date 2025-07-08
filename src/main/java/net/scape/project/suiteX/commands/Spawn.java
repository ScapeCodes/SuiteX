package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class Spawn extends BaseCommand {

    @Command(name = "spawn", permission = "suitex.command.spawn")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();

        String[] args = command.getArgs();

        player.teleport(SuiteX.getInstance().getConfigUtils().getSpawn());

        String teleporting_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("teleporting");
        teleporting_format = addPlaceholders(teleporting_format, player);
        msgPlayer(player, teleporting_format);
    }
}