package org.Richee.Models.Triggers;

import org.Richee.Core;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.Models.Course;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serial;

public class WinTrigger extends AbstractTrigger {
    @Serial
    private static final long serialVersionUID = 0L;

    @Override
    public void trigger(Player player, Course course) {
        player.sendMessage("You won! :D (To be implemented)");
        Core.publishEvent(new PlayerLeaveCourseEvent(
            player,
            course,
            PlayerLeaveCourseEvent.Reason.WIN
        ));
    }

    @Override
    public Material getIcon() {
        return Material.NETHER_STAR;
    }

    @Override
    public String getName() {
        return "win";
    }
}
