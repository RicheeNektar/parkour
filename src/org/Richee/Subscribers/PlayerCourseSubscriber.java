package org.Richee.Subscribers;

import org.Richee.DataContainer;
import org.Richee.Core;
import org.Richee.IO;
import org.Richee.Events.PlayerJoinCourseEvent;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class PlayerCourseSubscriber implements Listener {
    @EventHandler
    private void onPlayerJoinCourse(PlayerJoinCourseEvent event) {
        var player = event.getPlayer();

        if (null != DataContainer.getCourseFromPlayer(player)) {
            player.sendMessage(Translator.id(Prefix.INFO, "error.course.already_playing"));
            return;
        }

        try {
            IO.save(player);
        } catch (IOException e) {
            player.sendMessage("An error occurred while saving your data...");
            Core.logException(e);
            return;
        }

        var course = event.getCourse();
        DataContainer.setCourseForPlayer(player, course);
        player.teleport(course.config().getSpawn().toLocation());
        player.getInventory().clear();
    }

    @EventHandler
    private void onPlayerLeaveCourse(PlayerLeaveCourseEvent event) {
        var player = event.getPlayer();

        try {
            IO.load(player);
        } catch (IOException e) {
            player.sendMessage("An error occurred while loading your data...");
            Core.logException(e);
            return;
        }

        DataContainer.setCourseForPlayer(player, null);

        if (event.getCourse().isTest()) {
            try {
                IO.deleteCourse(event.getCourse().name());
            } catch (IOException ignored) {
                // We don't really care...
            }
        }
    }
}
