package org.Richee.Events;

import org.Richee.Models.Course;
import org.bukkit.entity.Player;

public class PlayerJoinCourseEvent extends AbstractEvent {
    private final Player player;
    private final Course course;

    public PlayerJoinCourseEvent(Player player, Course course) {
        this.player = player;
        this.course = course;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }
}
