package org.Richee.Models;

import org.bukkit.Bukkit;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Location implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 2L;

    public final float x, y, z;
    public final float pitch, yaw;
    public final String world;

    public Location(float x, float y, float z, float pitch, float yaw, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    public Location(org.bukkit.Location location) {
        this.world = Objects.requireNonNull(location.getWorld()).getUID().toString();

        this.x = (float) location.getX();
        this.y = (float) location.getY();
        this.z = (float) location.getZ();

        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public org.bukkit.Location toLocation() {
        return new org.bukkit.Location(getWorld(), x, y, z, yaw, pitch);
    }

    public org.bukkit.World getWorld() {
        return Bukkit.getWorld(UUID.fromString(this.world));
    }

    public Location below() {
        return new Location(
            this.x,
            this.y - 1,
            this.z,
            this.pitch,
            this.yaw,
            this.world
        );
    }

    public boolean greater(Location other) {
        return this.x >= other.x
            && this.y >= other.y
            && this.z >= other.z;
    }

    public boolean lower(Location other) {
        return this.x <= other.x
            && this.y <= other.y
            && this.z <= other.z;
    }

    public boolean in(Area area) {
        return getWorld() == area.pos1().getWorld()
            && this.greater(area.pos1())
            && this.lower(area.pos2());
    }

    @Override
    public Location clone() {
        try {
            return (Location) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Location location)) {
            return false;
        }

        return this.world.equals(location.world)
            && this.x == location.x
            && this.y == location.y
            && this.z == location.z
            && this.pitch == location.pitch
            && this.yaw == location.yaw;
    }
}
