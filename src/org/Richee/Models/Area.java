package org.Richee.Models;

import java.io.Serial;
import java.io.Serializable;

public class Area implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 0L;

    private Location pos1;
    private Location pos2;

    public Area(Location pos1, Location pos2) {
        this.pos1 = new Location(
            Math.floor(Math.min(pos1.x, pos2.x)),
            Math.floor(Math.min(pos1.y, pos2.y)),
            Math.floor(Math.min(pos1.z, pos2.z)),
            0,
            0,
            pos1.world
        );
        this.pos2 = new Location(
            Math.ceil(Math.max(pos1.x, pos2.x)),
            Math.ceil(Math.max(pos1.y, pos2.y)),
            Math.ceil(Math.max(pos1.z, pos2.z)),
            0,
            0,
            pos1.world
        );
    }

    public Location pos1() {
        return pos1;
    }

    public Location pos2() {
        return pos2;
    }

    @Override
    public Area clone() {
        try {
            var c = (Area) super.clone();
            c.pos1 = this.pos1.clone();
            c.pos2 = this.pos2.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Area area)) {
            return false;
        }

        return this.pos1().equals(area.pos1())
                && this.pos2().equals(area.pos2());
    }
}
