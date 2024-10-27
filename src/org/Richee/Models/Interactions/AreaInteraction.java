package org.Richee.Models.Interactions;

import org.Richee.Models.Area;
import org.Richee.Models.Location;
import org.Richee.Subscribers.PlayerInteractSubscriber;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.Consumer;

public class AreaInteraction extends AbstractInteraction<Area, PlayerInteractEvent> {
    private Location pos1;

    public AreaInteraction(Player p, Consumer<Area> consumer) {
        super(p, Translator.id("prompt.location", "Position 1"), consumer, PlayerInteractEvent.class);
    }

    @Override
    public boolean supports(PlayerInteractEvent event) {
        return event.getAction() != Action.PHYSICAL
            && event.getHand() == EquipmentSlot.HAND;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (pos1 == null) {
            PlayerInteractSubscriber.registerInteraction(event.getPlayer(), self());
            getValue(event);
            return;
        }
        if (this.getValue(event) == null) {
            PlayerInteractSubscriber.registerInteraction(event.getPlayer(), self());
            return;
        }
        super.onInteract(event);
    }

    @Override
    protected Area getValue(PlayerInteractEvent event) {
        event.setCancelled(true);

        var location = switch (event.getAction()) {
            case RIGHT_CLICK_AIR, LEFT_CLICK_AIR -> new Location(p.getLocation());
            case RIGHT_CLICK_BLOCK, LEFT_CLICK_BLOCK -> new Location(event.getClickedBlock().getLocation());
            default -> throw new IllegalStateException();
        };

        if (pos1 == null) {
            pos1 = location;
            p.sendMessage(Translator.id("prompt.location", "Position 2"));
            return null;
        }

        if (!pos1.getWorld().equals(location.getWorld())) {
            p.sendMessage(Translator.id("prompt.location.world_mismatch"));
            return null;
        }

        return new Area(pos1, location);
    }
}
