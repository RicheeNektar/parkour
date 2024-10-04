package org.Richee.Models;

import org.Richee.Exceptions.Validation.WorldMismatchLocationException;
import org.bukkit.Bukkit;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Location implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 2L;

    public final double x, y, z;
    public final float pitch, yaw;
    public final String world;

    public Location(double x, double y, double z, float pitch, float yaw, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    public Location(org.bukkit.Location location) {
        this.world = Objects.requireNonNull(location.getWorld()).getUID().toString();

        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();

        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public static Location[] toArea(Location pos1, Location pos2) throws WorldMismatchLocationException {
        if (pos1.world.equals(pos2.world)) {
            throw new WorldMismatchLocationException();
        }

        return new Location[] {
            new Location(
                Math.min(pos1.x, pos2.x),
                Math.min(pos1.y, pos2.y),
                Math.min(pos1.z, pos2.z),
                0,
                0,
                pos1.world
            ),
            new Location(
                Math.max(pos1.x, pos2.x),
                Math.max(pos1.y, pos2.y),
                Math.max(pos1.z, pos2.z),
                0,
                0,
                pos1.world
            ),
        };
    }

    public org.bukkit.Location toLocation() {
        return new org.bukkit.Location(Bukkit.getWorld(UUID.fromString(this.world)), x, y, z, yaw, pitch);
    }

    @Override
    public Location clone() {
        try {
            return (Location) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
