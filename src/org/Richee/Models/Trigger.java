package org.Richee.Models;

import org.Richee.Exceptions.Validation.WorldMismatchLocationException;

import java.io.Serial;
import java.io.Serializable;

public class Trigger implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final String name;
    public Location[] area;

    public Trigger(String name, Location pos1, Location pos2) throws WorldMismatchLocationException {
        this.name = name;
        this.area = Location.toArea(pos1, pos2);
    }
}
