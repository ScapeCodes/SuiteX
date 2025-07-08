package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class SetSpawn extends BaseCommand {

    @Command(name = "setspawn", permission = "suitex.command.setspawn")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();

        String[] args = command.getArgs();

        SuiteX.getInstance().getConfigUtils().setSpawn(player.getLocation());

        String spawn_set_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("spawn_set");
        spawn_set_format = addPlaceholders(spawn_set_format, player);
        msgPlayer(player, spawn_set_format);
    }
}