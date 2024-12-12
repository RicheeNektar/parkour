package org.Richee.Models.Interactions;

import org.Richee.Models.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.Consumer;

public class LocationInteraction extends AbstractInteraction<Location> {
    public LocationInteraction(Player p, String valueName, Consumer<Location> consumer) {
        super(p, valueName, consumer);
    }

    @Override
    public boolean supports(PlayerInteractEvent event) {
        return event.getAction() != Action.PHYSICAL
            && event.getHand() == EquipmentSlot.HAND;
    }

    @Override
    protected Location getValue(PlayerInteractEvent event) {
        event.setCancelled(true);

        return switch (event.getAction()) {
            case RIGHT_CLICK_AIR, LEFT_CLICK_AIR -> new Location(p.getLocation());
            case RIGHT_CLICK_BLOCK, LEFT_CLICK_BLOCK -> new Location(event.getClickedBlock().getLocation());
            default -> throw new IllegalStateException();
        };
    }
}
