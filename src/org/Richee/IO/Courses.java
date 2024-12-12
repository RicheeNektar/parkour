package org.Richee.IO;

import org.Richee.Core;
import org.Richee.Models.Course;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Courses {
    private static final HashMap<String, Course> courses = new HashMap<>();

    private static final String SUFFIX = ".course";
    private static final Path PATH = Paths.get(("plugins/" + Core.getPluginName() + "/courses/").replace('/', File.separatorChar));

    public static void loadAllCourses() throws IOException {
        if (Files.isDirectory(PATH)) {
            Files.walkFileTree(PATH, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (path.toString().endsWith(SUFFIX)) {
                        try (var ois = new ObjectInputStream(
                            new GZIPInputStream(
                                new FileInputStream(path.toFile())
                            )
                        )) {
                            var course = (Course) ois.readObject();
                            courses.put(course.name(), course);
                        } catch (Exception e) {
                            Core.log(Level.SEVERE, "load.error.course", path);
                            Core.logException(e);
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.createDirectories(PATH);
        }
    }

    public static boolean deleteCourse(String name) throws IOException {
        if (courses.containsKey(name)) {
            Files.deleteIfExists(PATH.resolve(name + SUFFIX));
            courses.remove(name);
            return true;
        }

        return false;
    }

    public static void save(Course course) throws IOException {
        if (!Files.isDirectory(PATH)) {
            Files.createDirectories(PATH);
        }

        courses.put(course.name(), course);

        if (course.isTest()) {
            return; // Do not persist test courses
        }

        var oos = new ObjectOutputStream(new GZIPOutputStream(
            new FileOutputStream(PATH.resolve(course.name() + SUFFIX).toFile())
        ));
        oos.writeObject(course);
        oos.close();
    }

    public static Course[] getCourses() {
        return courses.values().stream().filter(course -> !course.isTest()).toList().toArray(new Course[0]);
    }

    public static Course getCourse(String name) {
        return courses.get(name);
    }
}
