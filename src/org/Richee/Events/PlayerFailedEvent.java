package org.Richee.Events;

import org.Richee.Models.Course;
import org.bukkit.entity.Player;

public class PlayerFailedEvent extends AbstractEvent {
    public enum Reason {
        OUT_OF_BOUNDS, TRIGGER;
    }

    private final Player player;
    private final Course course;
    private final Reason reason;

    public PlayerFailedEvent(Player player, Course course, Reason reason) {
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
