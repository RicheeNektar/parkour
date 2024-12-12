package org.Richee.Commands;

import org.Richee.Core;
import org.Richee.Events.PlayerJoinCourseEvent;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.IO.Courses;
import org.Richee.IO.Players;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;

import static org.Richee.Events.PlayerLeaveCourseEvent.Reason.PLAYER_DECISION;

public class PlayCommand {
    @SubCommandExecutor(name = "join", alias = {"play"})
    public static void onJoin(Player player, String[] args) {
        var name = args[0];
        var course = Courses.getCourse(name);

        if (course == null || course.isTest()) {
            player.sendMessage(Translator.id(Prefix.INFO, "command.join.not_found", name));
            return;
        }

        if (!course.isReady()) {
            player.sendMessage(Translator.id(Prefix.INFO, "command.join.not_ready", name));
            return;
        }

        Core.publishEvent(new PlayerJoinCourseEvent(player, course));
    }

    @SubCommandExecutor(name = "leave")
    public static void onLeave(Player player, String[] args) {
        var course = Courses.getCourse(Players.getCourseFromPlayer(player));

        if (course == null) {
            player.sendMessage(Translator.id(Prefix.INFO, "command.leave.not_in_course"));
            return;
        }

        Core.publishEvent(new PlayerLeaveCourseEvent(player, course, PLAYER_DECISION));
    }
}
