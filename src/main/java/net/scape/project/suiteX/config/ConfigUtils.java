package net.scape.project.suiteX.config;

import net.scape.project.suiteX.SuiteX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class ConfigUtils {

    private final JavaPlugin plugin;

    public ConfigUtils(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setSpawn(Location location) {
        FileConfiguration spawn = SuiteX.getInstance().getConfigManager().getConfig("serverdata.yml").get();

        if (location == null || location.getWorld() == null) return;

        spawn.set("spawn.default.world", location.getWorld().getName());
        spawn.set("spawn.default.x", location.getX());
        spawn.set("spawn.default.y", location.getY());
        spawn.set("spawn.default.z", location.getZ());
        spawn.set("spawn.default.yaw", location.getYaw());
        spawn.set("spawn.default.pitch", location.getPitch());

        SuiteX.getInstance().getConfigManager().saveConfig("serverdata.yml");
    }

    public Location getSpawn() {
        FileConfiguration spawn = SuiteX.getInstance().getConfigManager().getConfig("serverdata.yml").get();

        Location location;

        if (spawn.getString("spawn.default.world") == null) {
            setSpawn(Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getSpawnLocation());
        }

        location = new Location(Bukkit.getWorld(Objects.requireNonNull(spawn.getString("spawn.default.world"))),
                spawn.getDouble("spawn.default.x"),
                spawn.getDouble("spawn.default.y"),
                spawn.getDouble("spawn.default.z"),
                (float) spawn.getDouble("spawn.default.yaw"),
                (float) spawn.getDouble("spawn.default.pitch"));

        return location;
    }

    public int getUnique() {
        return SuiteX.getInstance().getConfigManager().getConfig("serverdata.yml").get().getInt("unique-joins");
    }

    public String getFirstJoinMessage() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getString("join.first-join.string");
    }

    public String getCustomJoinMessage() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getString("join.enter.string");
    }

    public String getCustomQuitMessage() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getString("leave.string");
    }

    public boolean isFirstJoin() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getBoolean("join.first-join.enable");
    }

    public boolean isCustomJoin() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getBoolean("join.enter.enable");
    }

    public boolean isCustomQuit() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getBoolean("leave.enable");
    }

    public boolean isMOTDMessage() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getBoolean("join.motd.enable");
    }

    public List<String> getMOTDMessage() {
        return SuiteX.getInstance().getConfigManager().getConfig("events.yml").get().getStringList("join.motd.string");
    }

    public boolean isCreativeManager() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getBoolean("creative-manager.enable");
    }

    public boolean isDisableNetherite() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getBoolean("admin.disable-netherite");
    }

    public boolean isCMAlert() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getBoolean("creative-manager.alert-opped");
    }

    public String getCMAlert() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getString("creative-manager.alert");
    }

    public boolean isCMInventoryMove() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getBoolean("creative-manager.set-survival.inventory-move");
    }

    public boolean isCMWorldChange() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getBoolean("creative-manager.set-survival.world-change");
    }

    public boolean isCMOnTeleport() {
        return SuiteX.getInstance().getConfigManager().getConfig("admin-control.yml").get().getBoolean("creative-manager.set-survival.on-teleport");
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}