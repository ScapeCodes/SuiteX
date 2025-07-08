package net.scape.project.suiteX.commands;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.Utils;
import net.scape.project.suiteX.utils.commands.BaseCommand;
import net.scape.project.suiteX.utils.commands.Command;
import net.scape.project.suiteX.utils.commands.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class Gamemode extends BaseCommand {

    @Command(name = "gamemode", aliases = "gm", permission = "suitex.command.gamemode")
    @Override
    public void executeAs(CommandArguments command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sendUsage(player);
            return;
        }

        GameMode mode;
        switch (args[0].toLowerCase()) {
            case "0":
            case "survival":
            case "s":
                mode = GameMode.SURVIVAL;
                break;
            case "1":
            case "creative":
            case "c":
                mode = GameMode.CREATIVE;
                break;
            case "2":
            case "adventure":
            case "a":
                mode = GameMode.ADVENTURE;
                break;
            case "3":
            case "spectator":
                mode = GameMode.SPECTATOR;
                break;
            default:
                sendUsage(player);
                return;
        }

        if (args.length == 1) {
            if (player.hasPermission("suitex.command.gamemode." + mode.name().toUpperCase())) {
                player.setGameMode(mode);
                String gamemode_changed_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("gamemode_changed");
                gamemode_changed_format = gamemode_changed_format.replace("{0}", mode.name().toUpperCase());
                gamemode_changed_format = addPlaceholders(gamemode_changed_format, player);
                msgPlayer(player, gamemode_changed_format);

            } else {
                String no_perm_command_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("no_permission_command");
                no_perm_command_format = addPlaceholders(no_perm_command_format, player);

                msgPlayer(player, no_perm_command_format);
            }
        } else if (args.length == 2 && player.hasPermission("suitex.command.gamemode.others")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                target.setGameMode(mode);
                String gamemode_changed_other_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("gamemode_changed_other");
                gamemode_changed_other_format = gamemode_changed_other_format.replace("{0}", target.getName());
                gamemode_changed_other_format = gamemode_changed_other_format.replace("{1}", mode.name().toUpperCase());
                gamemode_changed_other_format = addPlaceholders(gamemode_changed_other_format, player);
                msgPlayer(player, gamemode_changed_other_format);

                String gamemode_changed_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("gamemode_changed");
                gamemode_changed_format = gamemode_changed_format.replace("{0}", mode.name().toUpperCase());
                gamemode_changed_format = addPlaceholders(gamemode_changed_format, target);
                msgPlayer(target, gamemode_changed_format);
            } else {
                String invalid_p_format = SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("invalid_player");
                invalid_p_format = addPlaceholders(invalid_p_format, player);
                msgPlayer(player, invalid_p_format);
            }
        } else {
            sendUsage(player);
        }
    }

    private void sendUsage(CommandSender sender) {
        Utils.msgPlayer(sender, "&cUsage: /gamemode <mode> [player]");
    }
}
