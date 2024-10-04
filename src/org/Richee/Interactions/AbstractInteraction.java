package org.Richee.Interactions;

import org.Richee.Subscribers.PlayerInteractSubscriber;
import org.bukkit.entity.Player;

import java.util.function.Function;

public abstract class AbstractInteraction<T> {
    private final Function<T, Void> callback;

    protected final Player p;

    public AbstractInteraction(Player p, String promptMessage, Function<T, Void> callback) {
        this.callback = callback;
        this.p = p;

        PlayerInteractSubscriber.registerInteraction(p, this);
        p.sendMessage(promptMessage);
    }

    public void onInteract() {
        this.callback.apply(this.getValue());
    }

    protected abstract T getValue();
}
