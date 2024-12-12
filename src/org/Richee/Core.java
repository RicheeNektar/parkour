package org.Richee;

import org.Richee.Commands.CourseCommandHandler;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.IO.Courses;
import org.Richee.IO.Players;
import org.Richee.Subscribers.*;
import org.Richee.Translations.Translator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core extends JavaPlugin {
    private static Core core;
    private static ConsoleCommandSender console;
    private static FileConfiguration config;
    private static PluginDescriptionFile description;
    private static PluginManager pluginManager;
    private static Logger logger;

    public void onLoad() {
        super.onLoad();

        Core.description = getDescription();
        Core.console = getServer().getConsoleSender();
        Core.config = getConfig();
        Core.logger = getLogger();

        config.options().copyDefaults(true);
        saveConfig();

        try {
            Translator.LoadTranslations(config.getString("language"));
        } catch (IOException | URISyntaxException e) {
            log(Level.SEVERE, "load.error.translations");
            logException(e);
        }

        try {
            Courses.loadAllCourses();
        } catch (IOException e) {
            log(Level.SEVERE, "load.error.courses");
            logException(e);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();

        var server = getServer();
        server.getPluginCommand("course").setExecutor(new CourseCommandHandler());

        pluginManager = server.getPluginManager();

        for (var handler : new Listener[] {
            new InventoryClickSubscriber(),
            new PlayerInteractSubscriber(),
            new PlayerJoinCourseSubscriber(),
            new PlayerLeaveCourseSubscriber(),
            new PlayerFailedSubscriber(),
            new PlayerMoveSubscriber(),
            new PlayerQuitSubscriber(),
        }) {
            pluginManager.registerEvents(handler, this);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (var p : getServer().getOnlinePlayers()) {
            var name = Players.getCourseFromPlayer(p);
            if (null != name) {
                var c = Courses.getCourse(name);
                if (null == c) {
                    Players.setCourseForPlayer(p, null);
                    p.saveData();
                } else {
                    Core.publishEvent(new PlayerLeaveCourseEvent(p, c, PlayerLeaveCourseEvent.Reason.SHUTDOWN));
                }
            }
        }
    }

    static String getPrefix(Prefix prefix) {
        return config.getString("prefix." + prefix.name().toLowerCase(), "");
    }

    public static void logException(Exception e) {
        var baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        log(Level.FINE, Charset.defaultCharset().decode(ByteBuffer.wrap(baos.toByteArray())).toString());
    }

    public static void log(Level level, String id, Object... params) {
        if (level.intValue() <= Level.FINE.intValue() && !config.getBoolean("debug")) {
            return;
        }
        console.sendMessage(Translator.id(Prefix.fromLogLevel(level), id, params));
    }

    public static String getPluginName() {
        return description.getName();
    }

    public static void publishEvent(Event event) {
        pluginManager.callEvent(event);
    }

    public static Core getPlugin() {
        return Core.core;
    }
}
