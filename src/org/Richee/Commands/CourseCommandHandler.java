package org.Richee.Commands;

import org.Richee.Core;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CourseCommandHandler implements CommandExecutor {
    private final HashMap<String, Method> commands = new HashMap<>();
    private final HashMap<String, ArrayList<String>> aliases = new HashMap<>();

    public CourseCommandHandler() {
        for (var c : new Class<?>[] {
            ManagementCommand.class,
            PlayCommand.class
        }) {
            for (var method : c.getDeclaredMethods()) {
                var annotation = method.getAnnotation(SubCommandExecutor.class);

                if (annotation != null) {
                    var cmd = annotation.name();
                    commands.put(cmd, method);

                    for (String alias : annotation.alias()) {
                        var list = aliases.containsKey(cmd) ? aliases.get(cmd) : new ArrayList<String>();
                        list.add(alias);
                        aliases.put(cmd, list);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("course")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Translator.id(Prefix.ERROR, "command.player_only"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(BuildHelp());
            return true;
        }

        var subCommand = args[0];
        if (!commands.containsKey(subCommand)) {
            sender.sendMessage(BuildHelp());
            return true;
        }

        var method = commands.get(subCommand);
        var params = Arrays.copyOfRange(args, 1, args.length);

        try {
            method.invoke(null, sender, params);

        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ArrayIndexOutOfBoundsException) {
                sender.sendMessage(BuildHelp(subCommand));
            } else {
                sender.sendMessage(Translator.id(Prefix.ERROR, "command.error"));
                Core.logException(e);
            }

        } catch (Exception e) {
            sender.sendMessage(Translator.id(Prefix.ERROR, "command.error"));
            Core.logException(e);
        }

        return true;
    }

    private String BuildHelp(String command) {
        var help = new StringBuilder()
            .append("&c")
            .append("/course ")
            .append(Translator.id("command." + command + ".help.usage"))
            .append("&b")
            .append(" - ")
            .append("&f")
            .append(Translator.id("command." + command + ".help"))
            .append("\n");

        var aliases = this.aliases.get(command);
        if (aliases != null) {
            StringBuilder tmp = new StringBuilder("Aliases: /course <");

            for (var alias : aliases) {
                tmp.append(alias).append("/");
            }

            help.append(tmp, 0, tmp.length() - 1)
                .append(">\n");
        }

        return ChatColor.translateAlternateColorCodes('&', help.toString());
    }

    private String BuildHelp() {
        StringBuilder help = new StringBuilder();

        help.append("&7========[ &bParkour Help &7]============\n");

        for (var cmd : commands.keySet()) {
            help.append(BuildHelp(cmd));
        }

        help.append("&7==================================");

        return ChatColor.translateAlternateColorCodes('&', help.toString());
    }
}
