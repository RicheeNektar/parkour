package org.Richee;

import org.Richee.Models.Course;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IO {
    private static final HashMap<String, Course> courses = new HashMap<>();

    private static final String COURSE_SUFFIX = ".course";
    private static final String PLAYER_SUFFIX = ".player";
    private static final Path coursePath = Paths.get(("plugins/" + Core.getPluginName() + "/courses/").replace('/', File.separatorChar));
    private static final Path playerPath = Paths.get(("plugins/" + Core.getPluginName() + "/players/").replace('/', File.separatorChar));

    public static void loadAllCourses() throws IOException {
        if (Files.isDirectory(coursePath)) {
            Files.walkFileTree(coursePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {

                    Core.log(Level.FINE, path.toString());

                    if (path.toString().endsWith(COURSE_SUFFIX)) {
                        try (var ois = new ObjectInputStream(
                            new GZIPInputStream(
                                new FileInputStream(path.toFile())
                            )
                        )) {
                            var course = (Course) ois.readObject();
                            courses.put(course.name(), course);
                        } catch (Exception e) {
                            Core.log(Level.SEVERE, "load.course.error", path);
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

    public static boolean deleteCourse(String name) throws IOException {
        if (courses.containsKey(name)) {
            Files.deleteIfExists(coursePath.resolve(name + COURSE_SUFFIX));
            courses.remove(name);
            return true;
        }

        return false;
    }

    public static void load(Player player) throws IOException {
        if (!Files.isDirectory(playerPath)) {
            Files.createDirectories(playerPath);
        }

        try (
            var bois = new BukkitObjectInputStream(
                new GZIPInputStream(
                    new FileInputStream(playerPath.resolve(player.getUniqueId() + PLAYER_SUFFIX).toFile())
                )
            )
        ) {
            var inventory = player.getInventory();
            var backup = (HashMap<String, Object>) bois.readObject();

            player.setHealth((double) backup.get("health"));
            player.setTotalExperience((int) backup.get("exp"));
            player.setFoodLevel((int) backup.get("food"));
            player.teleport((Location) backup.get("location"));

            var inventoryBackup = (ItemStack[]) backup.get("inventory");
            for (var i = 0; i < inventoryBackup.length; i++) {
                inventory.setItem(i, inventoryBackup[i]);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    public static void save(Player player) throws IOException {
        if (!Files.isDirectory(playerPath)) {
            Files.createDirectories(playerPath);
        }

        try (
            var boos = new BukkitObjectOutputStream(
                new GZIPOutputStream(
                    new FileOutputStream(playerPath.resolve(player.getUniqueId() + PLAYER_SUFFIX).toFile())
                )
            )
        ) {
            var buff = new HashMap<String, Object>();
            buff.put("health", player.getHealth());
            buff.put("exp", player.getTotalExperience());
            buff.put("food", player.getFoodLevel());
            buff.put("location", player.getLocation());
            buff.put("inventory", player.getInventory().getContents());
            boos.writeObject(buff);
            boos.flush();
        }
    }

    public static void save(Course course) throws IOException {
        if (!Files.isDirectory(coursePath)) {
            Files.createDirectories(coursePath);
        }

        courses.put(course.name(), course);

        if (course.isTest()) {
            return; // Do not persist test courses
        }

        var oos = new ObjectOutputStream(new GZIPOutputStream(
            new FileOutputStream(coursePath.resolve(course.name() + COURSE_SUFFIX).toFile())
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
