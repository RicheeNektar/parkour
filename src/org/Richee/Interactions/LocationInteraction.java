package org.Richee.Interactions;

import org.Richee.Models.Location;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class LocationInteraction extends AbstractInteraction<Location> {
    public LocationInteraction(Player p, String promptMessage, Function<Location, Void> callback) {
        super(p, promptMessage, callback);
    }

    @Override
    protected Location getValue() {
        return new Location(p.getLocation());
    }
}
