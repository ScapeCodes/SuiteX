package net.scape.project.suiteX.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.scape.project.suiteX.SuiteX;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.#");

    public static String format(String message) {
        message = message.replace(">>", "").replace("<<", "");
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
            String before = message.substring(0, matcher.start());
            String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String addPlaceholders(String string, Player player) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);
        int playTimeMinutes = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 60;
        String formattedTime = formatPlayTime(playTimeMinutes);

        //string = string.replace("{TPS}", String.valueOf(Lag.getTPS()));
        //string = string.replace("{FANCY_TPS}", Lag.getFancyTPS());
        string = string.replace("{KILLS}", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
        string = string.replace("{DEATHS}", String.valueOf(player.getStatistic(Statistic.DEATHS)));
        string = string.replace("{PING}", String.valueOf(player.getPing()));
        string = string.replace("{PLAYTIME}", formattedTime);
        string = string.replace("{PREFIX}", Objects.requireNonNull(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("prefix")));
        string = string.replace("{DATE}", formattedDate);
        string = string.replace("{UNIQUE}", String.valueOf(SuiteX.getInstance().getConfigUtils().getUnique()));
        string = string.replace("{ONLINE}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        string = string.replace("{MAX_ONLINE}", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        string = string.replace("{NAME}", player.getName());
        string = string.replace("{USERNAME}", player.getName());
        string = string.replace("{DISPLAYNAME}", player.getDisplayName());
        string = string.replace("{WORLDNAME}", player.getLocation().getWorld().getName());
        string = string.replace("{WORLD}", player.getLocation().getWorld().getName());
        string = string.replace("{X}", String.valueOf(player.getLocation().getX()));
        string = string.replace("{Y}", String.valueOf(player.getLocation().getY()));
        string = string.replace("{Z}", String.valueOf(player.getLocation().getZ()));
        string = string.replace("{XP}", String.valueOf(player.getLevel()));
        string = string.replace("{GAEMEMODE}", player.getGameMode().name());
        string = string.replace("{FLYING}", String.valueOf(player.isFlying()));
        string = replacePlaceholders(player, string);

        return string;
    }

    public static String addPlaceholders(String string, CommandSender player) {
        string = string.replace("{PREFIX}", Objects.requireNonNull(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("prefix")));
        string = string.replace("{ONLINE}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        string = string.replace("{MAX_ONLINE}", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        string = string.replace("{NAME}", player.getName());
        string = string.replace("{USERNAME}", player.getName());

        return string;
    }

    public static String formatPlayTime(long playTimeMinutes) {
        long months = playTimeMinutes / (60 * 24 * 30); // Assuming 30 days per month
        long weeks = (playTimeMinutes % (60 * 24 * 30)) / (60 * 24 * 7);
        long days = (playTimeMinutes % (60 * 24 * 7)) / (60 * 24);
        long hours = (playTimeMinutes % (60 * 24)) / 60;
        long minutes = playTimeMinutes % 60;

        StringBuilder formattedTime = new StringBuilder();
        if (months > 0) {
            formattedTime.append(months).append(" ").append(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("playtime_format.months")).append(" ");
        }
        if (weeks > 0) {
            formattedTime.append(weeks).append(" ").append(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("playtime_format.weeks")).append(" ");
        }
        if (days > 0) {
            formattedTime.append(days).append(" ").append(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("playtime_format.days")).append(" ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append(" ").append(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("playtime_format.hours")).append(" ");
        }
        if (minutes > 0 || formattedTime.length() == 0) {
            formattedTime.append(minutes).append(" ").append(SuiteX.getInstance().getConfigManager().getConfig("messages.yml").get().getString("playtime_format.minutes"));
        }

        return formattedTime.toString();
    }

    public static String deformat(String str) {
        return ChatColor.stripColor(format(str));
    }

    public static void msgPlayer(Player player, String... str) {
        for (String msg : str) {
            player.sendMessage(format(msg));
        }
    }

    public static String replacePlaceholders(Player p, String message) {
        String holders = message;

        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (PlaceholderAPI.containsPlaceholders(holders))
                holders = PlaceholderAPI.setPlaceholders(p, holders);
        }

        return holders;
    }

//    public static boolean isInRespectedKingdomLand(Player player) {
//        FileConfiguration config = SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("homes.yml").get();
//
//        if (config.getBoolean("homes.respect.kingdomsx") && Bukkit.getServer().getPluginManager().getPlugin("KingdomsX") != null) {
//            Land land = Land.getLand(player.getLocation());
//            if (land == null) return false;
//
//            if (land.isClaimed()) {
//                Kingdom kingdom = land.getKingdom();
//
//                if (kingdom == null) return false;
//
//                return !kingdom.isMember(player);
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    public static boolean isInRespectGPClaim(Player player) {
//        FileConfiguration config = SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("homes.yml").get();
//
//        if (config.getBoolean("homes.respect.griefprevention") && Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
//            DataStore dataStore = GriefPrevention.instance.dataStore;
//
//            Claim claim = dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
//
//            if (claim.checkPermission(player, ClaimPermission.Edit, null) != null) {
//                return true;
//            }
//
//            if (claim.checkPermission(player, ClaimPermission.Build, null) != null) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static boolean isInRespectedWGRegion(Player p) {
//        FileConfiguration config = SupremeCore.getsCore().getRegisterManager().getConfigManager().getConfig("homes.yml").get();
//
//        if (config.getBoolean("homes.respect.worldguard.enable") && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
//
//            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
//            RegionQuery query = container.createQuery();
//            ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
//
//            for (ProtectedRegion pr : set) {
//                for (String str : config.getStringList("homes.respect.worldguard.regions")) {
//                    if (pr.getId().equalsIgnoreCase(str)) {
//                        return true;
//                    }
//                }
//            }
//        }
//
//        return false;
//    }

    public static boolean isValidString(String str, String validChars) {
        if (str == null || str.isEmpty() || validChars == null || validChars.isEmpty()) {
            return false;
        }

        for (char ch : str.toCharArray()) {
            if (validChars.indexOf(ch) == -1) {
                return false;
            }
        }

        return true;
    }

//    public static String getRank(Player player) {
//        try {
//            if (!SupremeCore.getsCore().isPermsProvider()) {
//                return "default"; // Default rank if no permission provider is available
//            }
//
//            // Check if Vault permission provider is available
//            if (SupremeCore.getPermissions() == null) {
//                return "default"; // Default rank if no permission provider is available
//            }
//
//            // Get the player's primary group using Vault
//            String primaryGroup = SupremeCore.getPermissions().getPrimaryGroup(player);
//            if (primaryGroup != null) {
//                return primaryGroup;
//            } else {
//                return "default"; // Default rank if player's primary group cannot be retrieved
//            }
//        } catch (UnsupportedOperationException e) {
//            return "default";
//        }
//    }

    public static void msgPlayer(CommandSender player, String... str) {
        for (String msg : str) {
            player.sendMessage(format(msg));
        }
    }

    public static void titlePlayer(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(format(title), format(subtitle), 20 * fadeIn, 20 * stay, 20 * fadeOut);
    }

    public static void soundPlayer(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static List<String> color(List<String> lore) {
        return lore.stream().map(Utils::format).collect(Collectors.toList());
    }

    public static int timesJoined(OfflinePlayer player) {
        return player.getStatistic(Statistic.RECORD_PLAYED);
    }

    public static int compareVersions(String version1, String version2) {
        String[] splitVersion1 = version1.split("\\.");
        String[] splitVersion2 = version2.split("\\.");

        int length = Math.max(splitVersion1.length, splitVersion2.length);

        for (int i = 0; i < length; i++) {
            int v1 = i < splitVersion1.length ? Integer.parseInt(splitVersion1[i]) : 0;
            int v2 = i < splitVersion2.length ? Integer.parseInt(splitVersion2[i]) : 0;
            if (v1 < v2) {
                return -1;
            }
            if (v1 > v2) {
                return 1;
            }
        }
        return 0; // versions are equal
    }

    private static String formatLarge(double n, int iteration) {
        double f = n / 1000.0D;
        return f < 1000 || iteration >= getNumberFormat().length - 1 ?
                DECIMAL_FORMAT.format(f) + getNumberFormat()[iteration] : formatLarge(f, iteration + 1);
    }

    public static String formatNumber(double value) {
        return value < 1000 ? DECIMAL_FORMAT.format(value) : formatLarge(value, 0);
    }

    private static String[] getNumberFormat() {
        return "k;M;B;T;Q;QQ;S;SS;OC;N;D;UN;DD;TR;QT;QN;SD;SPD;OD;ND;VG;UVG;DVG;TVG;QTV;QNV;SEV;SPV;OVG;NVG;TG".split(";");
    }
}