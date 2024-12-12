package org.Richee.Models.Triggers;

import org.Richee.Core;
import org.Richee.Events.PlayerFailedEvent;
import org.Richee.Models.Course;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serial;

import static org.Richee.Events.PlayerFailedEvent.Reason.TRIGGER;

public class DeathTrigger extends AbstractTrigger {
    @Serial
    private static final long serialVersionUID = 0L;

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
