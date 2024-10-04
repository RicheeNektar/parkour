package org.Richee;

import org.Richee.Models.Course;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class CourseIO {
    public static List<Course> courses;

    private static final String SUFFIX = ".course";
    private static final Path coursePath = Paths.get(("plugins/" + Core.getPluginName() + "/courses/").replace('/', File.separatorChar));

    public static void loadAllCourses() throws IOException {
        courses = new ArrayList<>();

        if (Files.isDirectory(coursePath)) {
            Files.walkFileTree(coursePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {

                    Core.log(Severity.DEBUG, path.toString());

                    if (path.toString().endsWith(SUFFIX)) {
                        try (var ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            courses.add((Course) ois.readObject());
                        } catch (Exception e) {
                            Core.log(Severity.ERROR, "load.course.error", path);
                            Core.logException(e);
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.createDirectories(coursePath);
        }
    }

    public static void save(Course course) throws IOException {
        if (!Files.isDirectory(coursePath)) {
            Files.createDirectories(coursePath);
        }

        var oos = new ObjectOutputStream(new FileOutputStream(coursePath.resolve(course.toString() + SUFFIX).toFile()));
        oos.writeObject(course);
        oos.close();
    }
}
