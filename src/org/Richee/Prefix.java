package org.Richee;

import java.util.logging.Level;

public enum Prefix {
    ERROR,
    WARNING,
    INFO,
    NONE;

    public String toString() {
        return Core.getPrefix(this);
    }

    public static Prefix fromLogLevel(Level level) {
        if (level.intValue() >= Level.SEVERE.intValue()) {
            return Prefix.ERROR;
        } else if (level.intValue() >= Level.WARNING.intValue()) {
            return Prefix.WARNING;
        } else if (level.intValue() >= Level.INFO.intValue()) {
            return Prefix.INFO;
        }
        return Prefix.NONE;
    }
}
