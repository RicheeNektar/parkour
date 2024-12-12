package org.Richee.IO;

import org.Richee.Core;
import org.Richee.Models.Course;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Players {
    private static final String SUFFIX = ".player";
    private static final Path PATH = Paths.get(("plugins/" + Core.getPluginName() + "/players/").replace('/', File.separatorChar));
    private static final NamespacedKey COURSE_KEY = NamespacedKey.fromString("course", Core.getPlugin());
    private static final NamespacedKey CHECKPOINT_KEY = NamespacedKey.fromString("checkpoint", Core.getPlugin());

    public static void load(Player player) throws IOException {
        if (!Files.isDirectory(PATH)) {
            Files.createDirectories(PATH);
        }

        try (
            var bois = new BukkitObjectInputStream(
                new GZIPInputStream(
                    new FileInputStream(PATH.resolve(player.getUniqueId() + SUFFIX).toFile())
                )
            )
        ) {
            @SuppressWarnings("unchecked")
            var backup = (HashMap<String, Object>) bois.readObject();

            player.setHealth((double) backup.get("health"));
            player.setTotalExperience((int) backup.get("exp"));
            player.setFoodLevel((int) backup.get("food"));
            player.teleport((Location) backup.get("location"));

            var inventory = player.getInventory();
            var inventoryBackup = (ItemStack[]) backup.get("inventory");

            for (var i = 0; i < inventoryBackup.length; i++) {
                inventory.setItem(i, inventoryBackup[i]);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    public static void save(Player player) throws IOException {
        if (!Files.isDirectory(PATH)) {
            Files.createDirectories(PATH);
        }

        try (
            var boos = new BukkitObjectOutputStream(
                new GZIPOutputStream(
                    new FileOutputStream(PATH.resolve(player.getUniqueId() + SUFFIX).toFile())
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

    public static String getCourseFromPlayer(Player player) {
        return player.getPersistentDataContainer().get(COURSE_KEY, PersistentDataType.STRING);
    }

    public static int getCheckpointFromPlayer(Player player) {
        return player.getPersistentDataContainer().get(CHECKPOINT_KEY, PersistentDataType.INTEGER);
    }

    public static void setCourseForPlayer(Player player, Course course) {
        if (course == null) {
            player.getPersistentDataContainer().remove(COURSE_KEY);
        } else {
            player.getPersistentDataContainer().set(COURSE_KEY, PersistentDataType.STRING, course.name());
        }
    }

    public static void setCheckpointForPlayer(Player player, int id) {
        if (id < 0) {
            player.getPersistentDataContainer().remove(CHECKPOINT_KEY);
        } else {
            player.getPersistentDataContainer().set(CHECKPOINT_KEY, PersistentDataType.INTEGER, id);
        }
    }
}
