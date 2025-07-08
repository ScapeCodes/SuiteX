package net.scape.project.suiteX;

import net.scape.project.suiteX.config.ConfigUtils;
import net.scape.project.suiteX.listeners.PlayerListener;
import net.scape.project.suiteX.managers.ConfigManager;
import net.scape.project.suiteX.managers.CreativeManager;
import net.scape.project.suiteX.managers.xPlayerManager;
import net.scape.project.suiteX.utils.ClassRegistrationUtils;
import net.scape.project.suiteX.utils.commands.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SuiteX extends JavaPlugin {

    private static SuiteX instance;
    private ConfigManager configManager;
    private xPlayerManager playerManager;
    private CreativeManager creativeManager;

    private final CommandFramework commandFramework = new CommandFramework(this);

    private Boolean isFoliaFound;

    private ConfigUtils configUtils;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);

        isFoliaFound();

        Logger logger = Bukkit.getLogger();
        sendConsoleLog(logger);

        logger.info("> Loading Managers...");
        playerManager = new xPlayerManager(this.getDataFolder());
        creativeManager = new CreativeManager();

        logger.info("> Loading Commands & Listeners...");
        ClassRegistrationUtils.loadCommands("net.scape.project.suiteX.commands", this);
        ClassRegistrationUtils.loadListeners("net.scape.project.suiteX.listeners", this);

        configUtils = new ConfigUtils(this);
    }

    @Override
    public void onDisable() {
        // unload everything

        if (!isFoliaFound()) {
            this.getServer().getScheduler().cancelTasks(this);
        } else {
            // close folia schedulers.
        }

        // safely save all players leaving/kicked.
    }

    private void sendConsoleLog(Logger logger) {
        logger.info("");
        logger.info("  ____  _   _ ___ _____ _______  __");
        logger.info(" / ___|| | | |_ _|_   _| ____\\ \\/ /");
        logger.info(" \\___ \\| | | || |  | | |  _|  \\  / ");
        logger.info("  ___) | |_| || |  | | | |___ /  \\ ");
        logger.info(" |____/ \\___/|___| |_| |_____/_/\\_\\");
        logger.info(" The best core suited for your minecraft server!");
        logger.info("");
        logger.info("");
        logger.info("> Version: " + getDescription().getVersion());
        logger.info("> Author: Scape");
    }

    public static SuiteX getInstance() {
        return instance;
    }

    public void reloadCore() {
        super.reloadConfig();

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        getConfigManager().reloadConfig("messages.yml");
        getConfigManager().reloadConfig("economy.yml");
        getConfigManager().reloadConfig("admin-control.yml");
        getConfigManager().reloadConfig("serverdata.yml");
    }

    public xPlayerManager getPlayerManager() {
        return playerManager;
    }

    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    public boolean isFoliaFound() {
        if (this.isFoliaFound != null) {
            return this.isFoliaFound;
        }
        try {
            this.getLogger().info("[FOLIA] Found: " + Class.forName("io.papermc.paper.threadedregions.RegionizedServer"));
            this.getLogger().info("[SX Folia Version] Please report any compatibility issues with folia schedulers/features not working properly to ScapeHelp discord.");
            this.isFoliaFound = true;
        } catch (Exception ex) {
            this.isFoliaFound = false;
        }
        return this.isFoliaFound;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public CreativeManager getCreativeManager() {
        return creativeManager;
    }
}