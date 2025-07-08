package net.scape.project.suiteX.utils.commands;

import net.scape.project.suiteX.SuiteX;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static net.scape.project.suiteX.utils.Utils.addPlaceholders;
import static net.scape.project.suiteX.utils.Utils.msgPlayer;

public class CommandFramework implements CommandExecutor {

    private final SuiteX plugin;
    private final Map<String, Entry<Method, Object>> commandMap = new HashMap<>();
    private CommandMap map;

    public CommandFramework(SuiteX plugin) {
        this.plugin = plugin;
        this.map = resolveCommandMap();
    }

    private CommandMap resolveCommandMap() {
        // Paper API
        try {
            return Bukkit.getCommandMap();
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            // Fallback to reflection (for Spigot/Bukkit)
            try {
                PluginManager pluginManager = plugin.getServer().getPluginManager();
                Field field = pluginManager.getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                return (CommandMap) field.get(pluginManager);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, org.bukkit.command.@NotNull Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }

            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command annotation = method.getAnnotation(Command.class);

                if (!annotation.permission().isEmpty() && !sender.hasPermission(annotation.permission())) {
                    String noPerm = SuiteX.getInstance().getConfigManager()
                            .getConfig("messages.yml").get().getString("no_permission_command");
                    msgPlayer(sender, addPlaceholders(noPerm, sender));
                    return true;
                }

                if (annotation.inGameOnly() && !(sender instanceof Player)) {
                    String notAllowed = SuiteX.getInstance().getConfigManager()
                            .getConfig("messages.yml").get().getString("invalid_console_access");
                    msgPlayer(sender, addPlaceholders(notAllowed, sender));
                    return true;
                }

                try {
                    method.invoke(methodObject,
                            new CommandArguments(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        defaultCommand(new CommandArguments(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(Object obj, List<String> aliases) {
        for (Method method : obj.getClass().getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                Command command = method.getAnnotation(Command.class);
                if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != CommandArguments.class) {
                    System.out.println("Unable to register command " + method.getName() + ". Unexpected method arguments");
                    continue;
                }

                registerCommand(command, command.name(), method, obj);
                for (String alias : command.aliases()) {
                    registerCommand(command, alias, method, obj);
                }

                if (aliases != null) {
                    for (String alias : aliases) {
                        registerCommand(command, alias, method, obj);
                    }
                }

            } else if (method.getAnnotation(Completer.class) != null) {
                Completer comp = method.getAnnotation(Completer.class);
                if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != CommandArguments.class
                        || method.getReturnType() != List.class) {
                    System.out.println("Unable to register tab completer " + method.getName() + ". Unexpected method signature");
                    continue;
                }

                registerCompleter(comp.name(), method, obj);
                for (String alias : comp.aliases()) {
                    registerCompleter(alias, method, obj);
                }
            }
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));

        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }

        if (cmdLabel.equals(label)) {
            org.bukkit.command.Command cmd = map.getCommand(cmdLabel);
            if (!command.description().isEmpty()) cmd.setDescription(command.description());
            if (!command.usage().isEmpty()) cmd.setUsage(command.usage());
        }
    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command command = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), command);
        }

        org.bukkit.command.Command raw = map.getCommand(cmdLabel);

        if (raw instanceof BukkitCommand) {
            BukkitCommand cmd = (BukkitCommand) raw;
            if (cmd.completer == null) {
                cmd.completer = new BukkitCompleter();
            }
            cmd.completer.addCompleter(label, m, obj);
        } else if (raw instanceof PluginCommand) {
            try {
                Field field = raw.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                Object current = field.get(raw);
                if (current == null) {
                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(raw, completer);
                } else if (current instanceof BukkitCompleter) {
                    ((BukkitCompleter) current).addCompleter(label, m, obj);
                } else {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Already registered!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void defaultCommand(CommandArguments args) {
        args.getSender().sendMessage(args.getLabel() + " is not handled! Oh noes!");
    }

    public Map<String, Entry<Method, Object>> getCommandMap() {
        return commandMap;
    }
}
