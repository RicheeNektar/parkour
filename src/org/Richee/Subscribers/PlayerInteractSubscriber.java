package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Interactions.AbstractInteraction;
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var p = event.getPlayer();
        
        if (callbacks.containsKey(p)) {
            try {
                callbacks.get(p).onInteract();
            } catch (Exception e) {
                p.sendMessage(Translator.id("prompt.generic.error"));
                Core.logException(e);
                return;
            }

            callbacks.remove(p);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        callbacks.remove(event.getPlayer());
    }
}
