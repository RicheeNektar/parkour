package org.Richee.Models;

import org.Richee.IO.Courses;

public class TestCourse extends Course {
    public TestCourse(String name, CourseConfig config) {
        super("§§" + name, config);
    }

    public Course getOriginal() {
        return Courses.getCourse(this.name().substring(2));
    }
}
