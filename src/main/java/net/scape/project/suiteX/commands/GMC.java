package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class GMC extends BaseCommand {

    @Command(name = "gmc", aliases = "gm1", permission = "suitex.command.gamemode.creative")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();

        String[] args = command.getArgs();

        if (args.length == 0) {
            player.setGameMode(GameMode.CREATIVE);
            String gamemode_changed_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("gamemode_changed");
            gamemode_changed_format = gamemode_changed_format.replace("{0}", player.getGameMode().name().toUpperCase());
            gamemode_changed_format = addPlaceholders(gamemode_changed_format, player);
            msgPlayer(player, gamemode_changed_format);
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (player == target) {
                String self_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("self_invalid");
                self_format = addPlaceholders(self_format, player);
                msgPlayer(player, self_format);
                return;
            }

            if (target == null) {
                String invalid_p_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("invalid_player");
                invalid_p_format = addPlaceholders(invalid_p_format, player);
                msgPlayer(player, invalid_p_format);
                return;
            }

            target.setGameMode(GameMode.CREATIVE);
            String gamemode_changed_other_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("gamemode_changed_other");
            gamemode_changed_other_format = gamemode_changed_other_format.replace("{0}", target.getName());
            gamemode_changed_other_format = gamemode_changed_other_format.replace("{1}", target.getGameMode().name().toUpperCase());
            gamemode_changed_other_format = addPlaceholders(gamemode_changed_other_format, player);
            msgPlayer(player, gamemode_changed_other_format);

            String gamemode_changed_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("gamemode_changed");
            gamemode_changed_format = gamemode_changed_format.replace("{0}", target.getGameMode().name().toUpperCase());
            gamemode_changed_format = addPlaceholders(gamemode_changed_format, target);
            msgPlayer(target, gamemode_changed_format);
        }
    }
}