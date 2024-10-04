package org.Richee.Commands;

import org.Richee.Core;
import org.Richee.CourseIO;
import org.Richee.Models.Course;
import org.Richee.Menus.CourseList;
import org.Richee.Severity;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ManagementCommand {
    @SubCommandExecutor(name = "create")
    public static void onCreate(Player player, String[] args) {
        var name = args[0];
        var course = new Course(name);

        try {
            CourseIO.save(course);

        } catch (IOException e) {
            player.sendMessage(Translator.id(Severity.INFO, "command.create.error", name));
            Core.logException(e);
            return;
        }

        CourseIO.courses.add(course);
        player.sendMessage(Translator.id(Severity.INFO, "command.create.success", name));
    }

    @SubCommandExecutor(name = "delete")
    public static void onDelete(Player player, String[] args) {
        var name = args[0];

        for (int i = 0; i < CourseIO.courses.size(); i++) {
            if (name.equals(CourseIO.courses.get(i).toString())) {
                CourseIO.courses.remove(i);

                player.sendMessage(Translator.id(Severity.INFO, "command.delete.success", name));
                return;
            }
        }

        player.sendMessage(Translator.id(Severity.ERROR, "command.delete.not_found", name));
    }

    @SubCommandExecutor(name = "list")
    public static void onList(Player player, String[] args) {
        new CourseList(
            Translator.id("menu.course_list.title"),
            CourseIO.courses.toArray(new Course[0])
        ).open(player);
    }
}
