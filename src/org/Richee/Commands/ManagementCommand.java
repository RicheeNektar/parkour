package org.Richee.Commands;

import org.Richee.Core;
import org.Richee.IO.Courses;
import org.Richee.Menus.CourseList;
import org.Richee.Models.Course;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ManagementCommand {
    @SubCommandExecutor(name = "create")
    public static void onCreate(Player player, String[] args) {
        var name = args[0];

        if (Courses.getCourse(name) != null) {
            player.sendMessage(Translator.id(Prefix.INFO, "command.create.exists", name));
            return;
        }

        var course = new Course(name);

        try {
            Courses.save(course);

        } catch (IOException e) {
            player.sendMessage(Translator.id(Prefix.INFO, "command.create.error", name));
            Core.logException(e);
            return;
        }

        player.sendMessage(Translator.id(Prefix.INFO, "command.create.success", name));
    }

    @SubCommandExecutor(name = "delete")
    public static void onDelete(Player player, String[] args) {
        var name = args[0];

        try {
            if (Courses.deleteCourse(name)) {
                player.sendMessage(Translator.id(Prefix.INFO, "command.delete.success", name));
            } else {
                player.sendMessage(Translator.id(Prefix.ERROR, "command.delete.not_found", name));
            }
        } catch (IOException e) {
            player.sendMessage(Translator.id(Prefix.ERROR, "command.delete.error", name));
            Core.logException(e);
        }
    }

    @SubCommandExecutor(name = "list")
    public static void onList(Player player, String[] args) {
        new CourseList(Courses.getCourses()).open(player);
    }
}
