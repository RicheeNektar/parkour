package org.Richee.Menus;

import org.Richee.Core;
import org.Richee.Models.Course;
import org.Richee.Translations.Translator;
import org.bukkit.Material;

public class CourseList extends AbstractPaginationMenu<Course> {
    public CourseList(String title, Course[] courses) {
        super(title, courses);
    }

    @Override
    public void callback(Course course) {
        try {
            new CourseSetup(course).open(player);
        } catch (CloneNotSupportedException e) {
            player.sendMessage(Translator.id("menu.course_setup.error"));
            Core.logException(e);
        }
    }

    @Override
    public Material getMaterialForObject(Course course) {
        return course.isReady() ? Material.ENDER_EYE : Material.ENDER_PEARL;
    }
}
