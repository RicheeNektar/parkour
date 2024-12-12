package org.Richee.Models.Triggers;

import org.Richee.Models.Area;
import org.Richee.Models.Course;
import org.Richee.Models.Interactions.AreaInteraction;
import org.Richee.Translations.Translator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serializable;

public abstract class AbstractTrigger implements Serializable, Cloneable {
    public Area area;

    @Override
    public AbstractTrigger clone() {
        try {
            var c = (AbstractTrigger) super.clone();
            c.area = this.area.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    public void setup(Player player, Runnable callback) {
        new AreaInteraction(player, area -> {
            this.area = area;
            callback.run();
        });
    }

    @Override
    public String toString() {
        return Translator.id("trigger." + getName());
    }

    public abstract Material getIcon();
    public abstract String getName();

    public abstract void trigger(Player p, Course course);
}
