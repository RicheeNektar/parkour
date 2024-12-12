package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Models.Interactions.AbstractInteraction;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerInteractSubscriber implements Listener {
    private static final HashMap<Player, AbstractInteraction<?>> callbacks = new HashMap<>();

    public static void registerInteraction(Player p, AbstractInteraction<?> interaction) {
        callbacks.put(p, interaction);
    }

    private void handle(PlayerInteractEvent event) {
        var p = event.getPlayer();

        if (callbacks.containsKey(p)) {
            var interaction = callbacks.get(p);

            if (!interaction.supports(event)) {
                return;
            }
            callbacks.remove(p);

            try {
                interaction.onInteract(event);

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
