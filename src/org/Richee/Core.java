package org.Richee;

import org.Richee.Commands.CourseCommandHandler;
import org.Richee.Subscribers.InventoryClickSubscriber;
import org.Richee.Subscribers.PlayerInteractSubscriber;
import org.Richee.Translations.Translator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
    private static ConsoleCommandSender console;
    private static FileConfiguration config;
    private static PluginDescriptionFile description;
    private static Logger logger;

    public void onLoad() {
        saveConfig();

        Core.description = getDescription();
        Core.logger = getLogger();
        Core.config = getConfig();

        Core.console = getServer().getConsoleSender();

        try {
            Translator.LoadTranslations(config.getString("language"));
        } catch (IOException | URISyntaxException e) {
            log(Severity.ERROR, "load.translations.error");
            logException(e);
        }

        try {
            CourseIO.loadAllCourses();
        } catch (IOException e) {
            log(Severity.ERROR, "load.courses.error");
            logException(e);
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginCommand("course").setExecutor(new CourseCommandHandler());

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new InventoryClickSubscriber(), this);
        manager.registerEvents(new PlayerInteractSubscriber(), this);
    }

    public static void logException(Exception e) {
        var baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        log(Severity.DEBUG, Charset.defaultCharset().decode(ByteBuffer.wrap(baos.toByteArray())).toString());
    }

    public static void log(Severity severity, String id, Object... params) {
        if (severity == Severity.DEBUG && !config.getBoolean("debug")) {
            return;
        }

        logger.log(
            switch (severity) {
                case ERROR -> Level.SEVERE;
                case WARNING -> Level.WARNING;
                default -> Level.INFO;
            },
            Translator.id(severity, id, params)
        );
    }
    
    public static String getPrefix(Severity severity) {
        return switch (severity) {
            case ERROR -> config.getString("prefix.error");
            case WARNING -> config.getString("prefix.warn");
            case INFO -> config.getString("prefix.info");
            default -> "";
        };
    }

    public static String getPluginName() {
        return description.getName();
    }
}
