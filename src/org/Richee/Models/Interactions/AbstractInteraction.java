package org.Richee.Models.Interactions;

import org.Richee.Subscribers.PlayerInteractSubscriber;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

public abstract class AbstractInteraction<T> {
    private final Consumer<T> consumer;

    protected final Player p;

    public AbstractInteraction(Player p, String promptMessage, Consumer<T> consumer) {
        this.consumer = consumer;
        this.p = p;

        PlayerInteractSubscriber.registerInteraction(p, this);
        p.sendMessage(Translator.id("prompt.location", promptMessage));
    }

    public void onInteract(PlayerInteractEvent event) {
        this.consumer.accept(this.getValue(event));
    }

    public abstract boolean supports(PlayerInteractEvent event);

    protected abstract T getValue(PlayerInteractEvent event);

    protected AbstractInteraction<?> self()
    {
        return this;
    }
}
