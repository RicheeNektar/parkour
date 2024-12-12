package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Events.PlayerJoinCourseEvent;
import org.Richee.IO.Players;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class PlayerJoinCourseSubscriber implements Listener {
    @EventHandler
    private void onPlayerJoinCourse(PlayerJoinCourseEvent event) {
        var player = event.getPlayer();

        if (null != Players.getCourseFromPlayer(player)) {
            player.sendMessage(Translator.id(Prefix.INFO, "error.course.already_playing"));
            return;
        }

        try {
            Players.save(player);
        } catch (IOException e) {
            player.sendMessage("An error occurred while saving your data...");
            Core.logException(e);
            return;
        }

        var course = event.getCourse();

        player.teleport(course.config().getSpawn().toLocation());
        player.getInventory().clear();

        Players.setCheckpointForPlayer(player, 0);
        Players.setCourseForPlayer(player, course);
    }
}
