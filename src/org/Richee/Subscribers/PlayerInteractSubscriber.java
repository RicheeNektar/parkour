package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Models.Interactions.AbstractInteraction;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerInteractSubscriber implements Listener {
    private static final HashMap<Player, AbstractInteraction<?, PlayerEvent>> callbacks = new HashMap<>();

    public static void registerInteraction(Player p, AbstractInteraction<?, PlayerEvent> interaction) {
        callbacks.put(p, interaction);
    }

    private void handle(PlayerEvent event) {
        var p = event.getPlayer();

        if (callbacks.containsKey(p)) {
            var interaction = callbacks.get(p);
            var cast = interaction.cast(event);

            if (cast == null) { // Handler cannot handle event, at all
                return;
            }

            if (!interaction.supports(cast)) {
                return;
            }
            callbacks.remove(p);

            try {
                interaction.onInteract(cast);

            } catch (Exception e) {
                p.sendMessage(Translator.id(Prefix.ERROR, "generic.prompt.error"));
                Core.logException(e);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        handle(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        callbacks.remove(event.getPlayer());
    }
}
