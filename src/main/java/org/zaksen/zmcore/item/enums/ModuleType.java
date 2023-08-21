package org.zaksen.zmcore.item.enums;

import net.minecraft.util.StringIdentifiable;

public enum ModuleType implements StringIdentifiable {
    BLANK("blank"),
    SPEED("speed"),
    EFFICIENCY("efficiency"),
    PRODUCTIVITY("productivity");

    private final String name;
    private ModuleType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
