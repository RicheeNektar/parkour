package org.Richee.Models;

import java.io.Serial;
import java.io.Serializable;

public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final CourseConfig config;

    public Course(String name) {
        this(name, new CourseConfig());
    }

    public Course(String name, CourseConfig config) {
        this.name = name;
        this.config = config;
    }

    public String name() {
        return name;
    }

    public CourseConfig config() {
        return this.config;
    }

    public boolean isReady() {
        return this.config.validate().length == 0;
    }

    public boolean isTest() {
        return this instanceof TestCourse;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Course course)) {
            return false;
        }

        return this.name.equals(course.name)
            && this.config.equals(course.config);
    }
}
