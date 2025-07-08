package net.scape.project.suiteX.listeners;

import net.devscape.supremecore.SupremeCore;
import net.scape.project.suiteX.SuiteX;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.devscape.supremecore.utils.Utils.*;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (e.isCancelled()) return;

        if (SuiteX.getInstance().getSQLiteManager().getPlayer(player.getUniqueId()).isMuted()) {
            if (!player.isOp() || !player.hasPermission("score.exempt.mute")) {
                e.setCancelled(true);

                // player is muted & doesn't exempt mute chat
                // send muted message
            }
        }


        handleChatFormat(e);
    }

    private void handleChatFormat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        boolean enableChatFormat = SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("chat.yml").get().getBoolean("chat.enable");
        String originalMessage = e.getMessage();

        if (enableChatFormat) {
            boolean grouping = SupremeCore.getsCore().getRegisterManager().getConfigUtils().isListChatGroupEmpty();
            String rank = getRank(player);
            String chat;

            if (!grouping) {
                if (SupremeCore.getsCore().getRegisterManager().getConfigUtils().getChatGroup(rank) != null) {
                    chat = SupremeCore.getsCore().getRegisterManager().getConfigUtils().getChatGroup(rank);
                } else {
                    chat = SupremeCore.getsCore().getRegisterManager().getConfigUtils().getGlobalChatFormat();
                }
            } else {
                chat = SupremeCore.getsCore().getRegisterManager().getConfigUtils().getGlobalChatFormat();
            }

            if (chat != null) {
                String permission = SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("chat.yml").get().getString("chat.chat-color-permission");

                if (player.isOp()) {
                    if (!SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("player.yml").get().getString("player.ops-name-color").equalsIgnoreCase("none")) {
                        String colorcode = SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("player.yml").get().getString("player.ops-name-color");
                        player.setDisplayName(format(colorcode + player.getName()));
                    } else {
                        player.setDisplayName(format("&r" + player.getName()));
                    }
                } else {
                    player.setDisplayName(format(player.getName()));
                }

                chat = chat.replace("{MESSAGE}", format(originalMessage).replace("%", "%"));
                chat = addPlaceholders(chat, player);

                String formattedMessage = format(chat);

                formattedMessage = addPlaceholders(formattedMessage, player);

                assert permission != null;
                if (player.hasPermission(permission)) {
                    e.setFormat(formattedMessage.replace("{MESSAGE}", player.hasPermission(permission) ? format(originalMessage) : originalMessage).replaceAll("%", "%%").replaceAll("%%[^\\w\\s%]", ""));
                } else {
                    e.setFormat(formattedMessage.replace("{MESSAGE}", deformat(originalMessage)).replace("%", "%%").replaceAll("%[^\\w\\s%]", ""));
                }
            }
        }
    }
}
