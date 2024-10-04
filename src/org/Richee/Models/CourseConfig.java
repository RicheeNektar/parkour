package org.Richee.Models;

import org.Richee.Exceptions.Validation.WorldMismatchLocationException;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class CourseConfig implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 3L;

    private Location spawn;
    private Location[] area;

    private List<Trigger> triggers;

    /*
    * private HashMap<BlockAction, List<Block>> blockActions;
    */

    @Override
    public CourseConfig clone() throws CloneNotSupportedException {
        return (CourseConfig) super.clone();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location[] getArea() {
        return area;
    }

    public void setArea(Location pos1, Location pos2) throws WorldMismatchLocationException {
        this.area = Location.toArea(pos1, pos2);
    }



    public Trigger[] getTriggers() {
        return triggers.toArray(new Trigger[0]);
    }
}
