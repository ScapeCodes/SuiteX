package net.scape.project.suiteX.managers;

import net.scape.project.suiteX.handlers.xPlayer;
import net.scape.project.suiteX.handlers.xPlayerData;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class xPlayerManager {

    private final File userDataFolder;

    public xPlayerManager(File pluginDataFolder) {
        this.userDataFolder = new File(pluginDataFolder, "userdata");
        if (!userDataFolder.exists()) userDataFolder.mkdirs();
    }

    public xPlayer createPlayer(UUID uuid, String displayName) {
        File file = new File(userDataFolder, uuid.toString() + ".yml");
        if (file.exists()) {
            return get(uuid); // Already exists, just return the current data
        }

        xPlayer newPlayer = new xPlayer(
                displayName,
                "",                            // nickname
                System.currentTimeMillis(),    // lastSeen
                BigDecimal.ZERO,              // money
                BigDecimal.ZERO,              // bank
                List.of(),                    // homes
                false, false, false,          // isVanish, isMuted, isAFK
                false, false, false,          // isBaltopExempt, isGodMode, isTeleport
                false, true                   // isJailed, isAcceptingPay
        );

        save(uuid, newPlayer);
        return newPlayer;
    }

    public xPlayer get(UUID uuid) {
        File file = new File(userDataFolder, uuid.toString() + ".yml");
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String displayname = config.getString("displayname", "");
        String nickname = config.getString("nickname", "");
        long lastSeen = config.getLong("lastSeen", 0L);
        BigDecimal money = new BigDecimal(config.getString("money", "0.0"));
        BigDecimal bank = new BigDecimal(config.getString("bank", "0.0"));
        List<Location> homes = (List<Location>) config.getList("homes");

        boolean isVanish = config.getBoolean("isVanish", false);
        boolean isMuted = config.getBoolean("isMuted", false);
        boolean isAFK = config.getBoolean("isAFK", false);
        boolean isBaltopExempt = config.getBoolean("isBaltopExempt", false);
        boolean isGodMode = config.getBoolean("isGodMode", false);
        boolean isTeleport = config.getBoolean("isTeleport", false);
        boolean isJailed = config.getBoolean("isJailed", false);
        boolean isAcceptingPay = config.getBoolean("isAcceptingPay", true);

        return new xPlayer(displayname, nickname, lastSeen, money, bank, homes,
                isVanish, isMuted, isAFK, isBaltopExempt, isGodMode, isTeleport, isJailed, isAcceptingPay);
    }

    public void save(UUID uuid, xPlayer player) {
        File file = new File(userDataFolder, uuid.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("displayname", player.displayname());
        config.set("nickname", player.nickname());
        config.set("lastSeen", player.lastSeen());
        config.set("money", player.money().toPlainString());
        config.set("bank", player.bank().toPlainString());
        config.set("homes", player.homes());

        config.set("isVanish", player.isVanish());
        config.set("isMuted", player.isMuted());
        config.set("isAFK", player.isAFK());
        config.set("isBaltopExempt", player.isBaltopExempt());
        config.set("isGodMode", player.isGodMode());
        config.set("isTeleport", player.isTeleport());
        config.set("isJailed", player.isJailed());
        config.set("isAcceptingPay", player.isAcceptingPay());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(UUID uuid, Function<xPlayer, xPlayer> updater) {
        xPlayer current = get(uuid);
        if (current == null) return;

        xPlayer updated = updater.apply(current);
        save(uuid, updated);
    }

    public void updateWithData(UUID uuid, Consumer<xPlayerData> updater) {
        xPlayer current = get(uuid);
        if (current == null) return;

        xPlayerData data = new xPlayerData(current);
        updater.accept(data);
        save(uuid, data.toRecord());
    }
}
