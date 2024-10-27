package org.Richee.Models.Triggers;

import org.Richee.Models.Area;
import org.Richee.Models.Course;
import org.Richee.Translations.Translator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serializable;

public abstract class AbstractTrigger implements Serializable, Cloneable {
    public Area area;

    public AbstractTrigger(Area area) {
        this.area = area;
    }

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

    @Override
    public String toString() {
        return Translator.id("menu.trigger." + getName());
    }

    public String getLore() {
        return Translator.id(
            "menu.trigger.lore",
            area.pos1().getWorld().getName(),
            area.pos1().x,
            area.pos1().y,
            area.pos1().z,
            area.pos2().x,
            area.pos2().y,
            area.pos2().z
        );
    }

    public abstract Material getIcon();
    public abstract String getName();

    public abstract void trigger(Player p, Course course);
}
