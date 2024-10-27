package org.Richee;

import org.Richee.Models.Course;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class DataContainer {
    private static final NamespacedKey CourseKey = NamespacedKey.fromString("course", Core.getPlugin());

    public static String getCourseFromPlayer(Player player) {
        return player.getPersistentDataContainer().get(CourseKey, PersistentDataType.STRING);
    }

    public static void setCourseForPlayer(Player player, Course course) {
        if (course == null) {
            player.getPersistentDataContainer().remove(CourseKey);
        } else {
            player.getPersistentDataContainer().set(CourseKey, PersistentDataType.STRING, course.name());
        }
    }
}
