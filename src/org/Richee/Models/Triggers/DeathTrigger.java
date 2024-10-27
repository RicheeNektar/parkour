package org.Richee.Models.Triggers;

import org.Richee.Core;
import org.Richee.Events.PlayerFailedEvent;
import org.Richee.Models.Area;
import org.Richee.Models.Course;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static org.Richee.Events.PlayerFailedEvent.Reason.TRIGGER;

public class DeathTrigger extends AbstractTrigger {
    public DeathTrigger(Area area) {
        super(area);
    }

    @Override
    public void trigger(Player player, Course course) {
        Core.publishEvent(new PlayerFailedEvent(
            player,
            course,
            TRIGGER
        ));
    }

    @Override
    public Material getIcon() {
        return Material.SKELETON_SKULL;
    }

    @Override
    public String getName() {
        return "death";
    }
}
