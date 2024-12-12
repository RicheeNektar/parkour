package org.Richee.Menus;

import org.Richee.Models.Course;
import org.Richee.Translations.Translator;
import org.bukkit.Material;

public class CourseList extends AbstractPaginationMenu<Course> {
    public CourseList(Course[] courses) {
        super(Translator.id("menu.course.list.title"), courses);
    }

    @Override
    public void callback(Course course) {
        new CourseSetup(course).open(player);
    }

    @Override
    public Material getMaterialForObject(Course course) {
        return course.isReady() ? Material.ENDER_EYE : Material.ENDER_PEARL;
    }
}
