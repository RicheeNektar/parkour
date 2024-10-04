package org.Richee.Models;

import java.io.Serial;
import java.io.Serializable;

public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private CourseConfig courseConfig = new CourseConfig();

    public Course(String name) {
        this.name = name;
    }

    public CourseConfig getConfig() throws CloneNotSupportedException {
        return courseConfig.clone();
    }

    public void setConfig(CourseConfig config) throws CloneNotSupportedException {
        this.courseConfig = config.clone();
    }

    public boolean isReady() {
        return courseConfig.getArea() != null && courseConfig.getArea().length == 2 && courseConfig.getSpawn() != null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
