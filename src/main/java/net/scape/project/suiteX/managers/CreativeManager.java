package net.scape.project.suiteX.managers;

import net.scape.project.suiteX.SuiteX;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class CreativeManager {

    public void check(Player player) {
        if (!player.hasPermission("score.creativemanager.bypass")) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                player.setGameMode(GameMode.SURVIVAL);

                alertOps(player);
            }
        }
    }

    public void alertOps(Player player) {
        if (SuiteX.getInstance().getConfigUtils().isCMAlert()) {
            for (Player ops : Bukkit.getOnlinePlayers()) {
                if (ops.isOp()) {
                    String alert = SuiteX.getInstance().getConfigUtils().getCMAlert();
                    alert = alert.replace("{0}", player.getName());
                    alert = addPlaceholders(alert, player);

                    msgPlayer(ops, alert);
                }
            }
        }
    }
}
