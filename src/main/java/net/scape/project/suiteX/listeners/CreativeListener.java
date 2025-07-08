package net.scape.project.suiteX.listeners;

import net.scape.project.suiteX.SuiteX;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CreativeListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        if (SuiteX.getInstance().getConfigUtils().isCreativeManager()) {
            if (SuiteX.getInstance().getConfigUtils().isCMWorldChange()) {
                SuiteX.getInstance().getCreativeManager().check(player);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();

        if (SuiteX.getInstance().getConfigUtils().isCreativeManager()) {
            if (SuiteX.getInstance().getConfigUtils().isCMOnTeleport()) {
                SuiteX.getInstance().getCreativeManager().check(player);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (SuiteX.getInstance().getConfigUtils().isCreativeManager()) {
            if (SuiteX.getInstance().getConfigUtils().isCMWorldChange()) {
                SuiteX.getInstance().getCreativeManager().check(player);
            }
        }
    }
}
