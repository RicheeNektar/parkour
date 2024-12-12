package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Events.PlayerFailedEvent;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.IO.Courses;
import org.Richee.IO.Players;
import org.Richee.Models.Location;
import org.Richee.Models.Triggers.CheckpointTrigger;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class PlayerFailedSubscriber implements Listener {
    @EventHandler
    public void onPlayerFailed(PlayerFailedEvent event) {
        var player = event.getPlayer();

        var checkpoint = Players.getCheckpointFromPlayer(player);
        var course = Courses.getCourse(Players.getCourseFromPlayer(player));
        var config = course.config();

        Location respawn;

        if (checkpoint > 0) {
            var trigger = config.getTriggers()[checkpoint];

            if (!(trigger instanceof CheckpointTrigger)) {
                Core.publishEvent(new PlayerLeaveCourseEvent(
                    player,
                    course,
                    PlayerLeaveCourseEvent.Reason.ERROR
                ));

                Core.log(Level.SEVERE, "Unexpected trigger of type " + trigger.getClass().getName());
                player.sendMessage(Translator.id(Prefix.ERROR, "generic.error"));
                return;
            }

            respawn = ((CheckpointTrigger) trigger).getRespawnPoint();
        } else {
            respawn = config.getSpawn();
        }

        player.teleport(respawn.toLocation());
    }
}
