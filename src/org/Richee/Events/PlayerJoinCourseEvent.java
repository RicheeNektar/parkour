package org.Richee.Events;

import org.Richee.Models.Course;
import org.bukkit.entity.Player;

public class PlayerJoinCourseEvent extends AbstractEvent {
    private final Player player;
    private final Course course;
    private final boolean isTesting;

    public PlayerJoinCourseEvent(Player player, Course course, boolean isTesting) {
        this.player = player;
        this.course = course;
        this.isTesting = isTesting;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }

    public boolean isTesting() {
        return isTesting;
    }
}
