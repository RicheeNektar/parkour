package org.Richee.Commands;

import org.Richee.Core;
import org.Richee.DataContainer;
import org.Richee.Events.PlayerJoinCourseEvent;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.IO;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;

import static org.Richee.Events.PlayerLeaveCourseEvent.Reason.PLAYER_DECISION;

public class PlayCommand {
    @SubCommandExecutor(name = "join", alias = {"play"})
    public static void onJoin(Player player, String[] args) {
        var name = args[0];
        var course = IO.getCourse(name);

        if (course == null || course.isTest()) {
            player.sendMessage("command.join.not_found");
            return;
        }

        if (!course.isReady()) {
            player.sendMessage("command.join.not_ready");
            return;
        }

        Core.publishEvent(new PlayerJoinCourseEvent(player, course, false));
    }

    @SubCommandExecutor(name = "leave")
    public static void onLeave(Player player, String[] args) {
        var course = IO.getCourse(DataContainer.getCourseFromPlayer(player));

        if (course == null) {
            player.sendMessage(Translator.id("command.leave.not_in_course"));
            return;
        }

        Core.publishEvent(new PlayerLeaveCourseEvent(player, course, PLAYER_DECISION));
    }
}
