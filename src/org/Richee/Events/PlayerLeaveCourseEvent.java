package org.Richee.Events;

import org.Richee.Models.Course;
import org.bukkit.entity.Player;

public class PlayerLeaveCourseEvent extends AbstractEvent {
    public enum Reason {
        WIN, MAX_DEATHS_REACHED, PLAYER_DECISION, SHUTDOWN;
    }

    private final Player player;
    private final Course course;
    private final Reason reason;

    public PlayerLeaveCourseEvent(Player player, Course course, Reason reason) {
        this.player = player;
        this.course = course;
        this.reason = reason;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }

    public Reason getReason() {
        return reason;
    }
}
