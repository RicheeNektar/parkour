package org.Richee.Models.Interactions;

import org.Richee.Subscribers.PlayerInteractSubscriber;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import java.util.function.Consumer;

public abstract class AbstractInteraction<T, E extends PlayerEvent> {
    private final Consumer<T> consumer;
    private final Class<E> eClass;

    protected final Player p;

    @SuppressWarnings("unchecked")
    public AbstractInteraction(Player p, String promptMessage, Consumer<T> consumer, Class<E> eClass) {
        this.consumer = consumer;
        this.eClass = eClass;
        this.p = p;

        PlayerInteractSubscriber.registerInteraction(p, (AbstractInteraction<?, PlayerEvent>) this);
        p.sendMessage(promptMessage);
    }

    @SuppressWarnings("unchecked")
    public E cast(PlayerEvent event) {
        if (this.eClass.isInstance(event)) {
            return (E) event;
        }
        return null;
    }

    public void onInteract(E event) {
        this.consumer.accept(this.getValue(event));
    }

    public abstract boolean supports(E event);

    protected abstract T getValue(E event);

    @SuppressWarnings("unchecked")
    protected AbstractInteraction<?, PlayerEvent> self()
    {
        return (AbstractInteraction<?, PlayerEvent>) this;
    }
}
