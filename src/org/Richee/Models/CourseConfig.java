package org.Richee.Models;

import org.Richee.Models.Triggers.AbstractTrigger;
import org.Richee.Models.Triggers.WinTrigger;
import org.Richee.Translations.Translator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseConfig implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 4L;

    private Location spawn;
    private Area area;
    private List<AbstractTrigger> triggers;

    /*
    * private HashMap<BlockAction, List<Block>> blockActions;
    */

    public CourseConfig(Location spawn, Area area, List<AbstractTrigger> triggers) {
        this.spawn = spawn;
        this.area = area;
        this.triggers = triggers;
    }

    public CourseConfig() {
        this.spawn = null;
        this.area = null;
        this.triggers = new ArrayList<>();
    }

    public Location getSpawn() {
        return spawn;
    }

    public Area getArea() {
        return area;
    }

    public AbstractTrigger[] getTriggers() {
        return triggers.toArray(new AbstractTrigger[0]);
    }

    public String[] validate() {
        var errors = new ArrayList<String>();

        if (spawn == null) {
            errors.add(Translator.id("error.course.spawn.null"));

        } else if (spawn.getWorld() == null) {
            errors.add(Translator.id("error.course.spawn.world_null"));
        }

        if (area == null) {
            errors.add(Translator.id("error.course.area.null"));

        } else if (area.pos1().getWorld() == null) {
            errors.add(Translator.id("error.course.area.world_null"));

        } else {
            if (!spawn.in(area)) {
                errors.add(Translator.id("error.course.spawn.out_of_bounds"));
            }

            var hasAtLeastOneWinTrigger = false;

            for (var trigger : triggers) {
                if (!(
                    trigger.area.pos1().in(area)
                    && trigger.area.pos2().in(area)
                )) {
                    errors.add(Translator.id("error.course.trigger.out_of_bounds"));
                }
                if (trigger instanceof WinTrigger) {
                    hasAtLeastOneWinTrigger = true;
                }
            }

            if (!hasAtLeastOneWinTrigger) {
                errors.add(Translator.id("error.course.no_end"));
            }
        }

        return errors.toArray(new String[0]);
    }

    @Override
    public CourseConfig clone() {
        try {
            CourseConfig c = (CourseConfig) super.clone();

            if (null != this.spawn) {
                c.spawn = this.spawn.clone();
            }

            if (null != this.area) {
                c.area = this.area.clone();
            }

            c.triggers = new ArrayList<>(c.triggers);

            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CourseConfig config)) {
            return false;
        }

        return Objects.equals(this.getSpawn(), config.getSpawn())
            && Objects.equals(this.getArea(), config.getArea())
            && Objects.deepEquals(this.getTriggers(), config.getTriggers());
    }
}
