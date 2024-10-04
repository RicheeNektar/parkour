package org.Richee.Commands;

import org.Richee.Core;
import org.Richee.Severity;
import org.Richee.Translations.Translator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

public class CourseCommandHandler implements CommandExecutor {
    private final HashMap<String, Method> methods = new HashMap<>();

    public CourseCommandHandler() {
        for (var c : new Class<?>[] {
            ManagementCommand.class
        }) {
            for (var method : c.getDeclaredMethods()) {
                var annotation = method.getAnnotation(SubCommandExecutor.class);

                if (annotation != null) {
                    methods.put(annotation.name(), method);
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
            sender.sendMessage(Translator.id(Severity.ERROR, "command.player_only"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(BuildHelp());
            return true;
        }

        var subCommand = args[0];
        if (!methods.containsKey(subCommand)) {
            sender.sendMessage(BuildHelp());
            return true;
        }

        var method = methods.get(subCommand);
        var params = Arrays.copyOfRange(args, 1, args.length);

        try {
            method.invoke(null, sender, params);

        } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage(BuildHelp(subCommand));

        } catch (Exception e) {
            sender.sendMessage(Translator.id(Severity.ERROR, "command.error"));
            Core.logException(e);
        }

        return true;
    }

    private String BuildHelp() {
        return BuildHelp("\\w+");
    }

    private String BuildHelp(String subCommand) {
        String[] commands = Translator.ids("^command\\." + subCommand + "\\.help$");
        StringBuilder help = new StringBuilder();

        help.append("&7========[ &bParkour Help &7]============\n");

        for (String command : commands) {
            String cmdLabel = "/course " + Translator.id(Severity.DEBUG, command + ".usage");
            String cmdDesc = Translator.id(Severity.DEBUG, command);

            help.append("&c")
                .append(cmdLabel)
                .append("&b")
                .append(" - ")
                .append("&f")
                .append(cmdDesc)
                .append('\n');
        }

        help.append("&7==================================");

        return ChatColor.translateAlternateColorCodes('&', help.toString());
    }
}
