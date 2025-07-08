package net.scape.project.suiteX.listeners;

import net.scape.project.suiteX.SuiteX;
import net.scape.project.suiteX.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.scape.project.suiteX.utils.Utils.*;

public class JoinLeaveEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        SuiteX.getInstance().getPlayerManager().createPlayer(player.getUniqueId(), player.getDisplayName());
        if (player.isOp()) {
            Utils.msgPlayer(player,
                    "&c&m--------------------------------------------------",
                    "&c&l⚠ WARNING: Development Build ⚠",
                    "",
                    "&7You are currently running a &cdevelopment build &7of this plugin.",
                    "&7Some features may be incomplete or unstable.",
                    "",
                    "&eNote: &7This message is for internal or testing builds.",
                    "&7Only visible to &cadmins &7and &coperators&7.",
                    "&c&m--------------------------------------------------"
            );
        }

        if (!player.hasPlayedBefore()) {
            // increase the unique total joins.
            FileConfiguration smalldata = SuiteX.getInstance().getConfigManager().getConfig("serverdata.yml").get();
            smalldata.set("unique-join", smalldata.getInt("unique-join") + 1);
            SuiteX.getInstance().getConfigManager().saveConfig("serverdata.yml");

            if (!SuiteX.getInstance().getConfigUtils().getFirstJoinMessage().equalsIgnoreCase("none")) {
                String first_join = SuiteX.getInstance().getConfigUtils().getFirstJoinMessage();
                first_join = addPlaceholders(first_join, player);

                Bukkit.broadcastMessage(format(first_join));
                e.setJoinMessage(null);
            }
        } else {
            if (!SuiteX.getInstance().getConfigUtils().getCustomJoinMessage().equalsIgnoreCase("none")) {
                String join = SuiteX.getInstance().getConfigUtils().getCustomJoinMessage();
                join = addPlaceholders(join, player);

                e.setJoinMessage(format(join));
            }

            if (SuiteX.getInstance().getConfigUtils().isMOTDMessage()) {
                for (String str : SuiteX.getInstance().getConfigUtils().getMOTDMessage()) {
                    str = addPlaceholders(str, player);
                    msgPlayer(player, str);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        SuiteX.getInstance().getPlayerManager().updateWithData(e.getPlayer().getUniqueId(), data -> {
            data.setLastSeen(System.currentTimeMillis());
        });

        if (!SuiteX.getInstance().getConfigUtils().getCustomQuitMessage().equalsIgnoreCase("none")) {
            String quit = SuiteX.getInstance().getConfigUtils().getCustomQuitMessage();
            quit = addPlaceholders(quit, player);

            e.setQuitMessage(format(quit));
        }
    }

//    @EventHandler(priority = EventPriority.HIGH)
//    public void onTeleport(PlayerTeleportEvent e) {
//        Player player = e.getPlayer();
//
//        sPlayer sp = SuiteX.getInstance().getPlayerManager().get(player);
//        sp.setLastLocation(e.getFrom());
//
//        SuiteX.getInstance().getPlayerManager().updateCache(player.getUniqueId(), sp);
//    }
}