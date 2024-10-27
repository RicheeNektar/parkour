package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.DataContainer;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.IO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitSubscriber implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        var p = quitEvent.getPlayer();
        var c = IO.getCourse(DataContainer.getCourseFromPlayer(p));

        if (c != null) {
            Core.publishEvent(new PlayerLeaveCourseEvent(p, c, PlayerLeaveCourseEvent.Reason.PLAYER_DECISION));
        }
    }
}
