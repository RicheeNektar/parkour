package org.Richee.Models;

public class TestCourse extends Course {
    public TestCourse(String name, CourseConfig config) {
        super((name.startsWith("TEST") ? "" : "TEST") + name, config);
    }
}
