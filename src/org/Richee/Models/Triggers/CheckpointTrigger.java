package org.Richee.Models.Triggers;

import org.Richee.IO.Players;
import org.Richee.Models.Course;
import org.Richee.Models.Interactions.LocationInteraction;
import org.Richee.Models.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serial;
import java.util.Arrays;

public class CheckpointTrigger extends AbstractTrigger {
    @Serial
    private static final long serialVersionUID = 0L;

    private Location respawnPoint;

    public Location getRespawnPoint() {
        return respawnPoint;
    }

    @Override
    public void setup(Player player, Runnable callback) {
        super.setup(
            player,
            () -> new LocationInteraction(
                player,
                "Respawn Point",
                respawnPoint -> {
                    this.respawnPoint = respawnPoint;
                    callback.run();
                }
            )
        );
    }

    @Override
    public void trigger(Player player, Course course) {
        var id = Arrays.asList(
            course.config().getTriggers()
        ).indexOf(this);

        if (Players.getCheckpointFromPlayer(player) < id) {
            Players.setCheckpointForPlayer(player, id);
        }
    }

    @Override
    public Material getIcon() {
        return Material.RED_BED;
    }

    @Override
    public String getName() {
        return "checkpoint";
    }
}
