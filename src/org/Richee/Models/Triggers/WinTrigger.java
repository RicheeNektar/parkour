package org.Richee.Models.Triggers;

import org.Richee.Core;
import org.Richee.Events.PlayerLeaveCourseEvent;
import org.Richee.Models.Area;
import org.Richee.Models.Course;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WinTrigger extends AbstractTrigger {
    public WinTrigger(Area area) {
        super(area);
    }

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
