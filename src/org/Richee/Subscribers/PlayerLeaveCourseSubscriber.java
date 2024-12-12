package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.IO.Courses;
import org.Richee.IO.Players;
import org.Richee.Menus.CourseSetup;
import org.Richee.Models.TestCourse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class PlayerLeaveCourseSubscriber implements Listener {
    @EventHandler
    private void onPlayerLeaveCourse(PlayerLeaveCourseEvent event) {
        var player = event.getPlayer();

        try {
            Players.load(player);
        } catch (IOException e) {
            player.sendMessage("An error occurred while loading your data...");
            Core.logException(e);
            return;
        }

        Players.setCourseForPlayer(player, null);
        Players.setCheckpointForPlayer(player, -1);

        if (event.getCourse().isTest()) {
            var testCourse = (TestCourse) event.getCourse();

            try {
                Courses.deleteCourse(testCourse.name());
            } catch (IOException ignored) {
                // We don't really care...
            }

            new CourseSetup(testCourse.getOriginal(), testCourse.config()).open(player);
        }
    }
}
