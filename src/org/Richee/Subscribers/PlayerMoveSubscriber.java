package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.DataContainer;
import org.Richee.Events.PlayerFailedEvent;
import org.Richee.IO;
import org.Richee.Models.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static org.Richee.Events.PlayerFailedEvent.Reason.OUT_OF_BOUNDS;

public class PlayerMoveSubscriber implements Listener {
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var name = DataContainer.getCourseFromPlayer(player);
        if (name == null) {
            return;
        }

        var course = IO.getCourse(name);
        if (course == null) {
            return;
        }

        var playerPos = new Location(player.getLocation());
        var config = course.config();
        var area = config.getArea();

        if (!playerPos.in(area)) {
            Core.publishEvent(
                new PlayerFailedEvent(
                    player,
                    course,
                    OUT_OF_BOUNDS
                )
            );
            return;
        }

        for (var trigger : config.getTriggers()) {
            if (playerPos.in(trigger.area)) {
                trigger.trigger(player, course);
            }
        }

        // @todo block actions
        playerPos.below().toLocation().getBlock();
    }
}
