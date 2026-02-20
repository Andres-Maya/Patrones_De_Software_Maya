package com.wargame.enums;

/**
 * Represents all available weapon types for soldiers.
 */
public enum WeaponType {
    ASSAULT_RIFLE("Assault Rifle", 75, "⚔"),
    SNIPER_RIFLE("Sniper Rifle", 95, "🎯"),
    SHOTGUN("Shotgun", 85, "💥"),
    MACHINE_GUN("Machine Gun", 70, "🔫"),
    ROCKET_LAUNCHER("Rocket Launcher", 100, "🚀"),
    PISTOL("Pistol", 40, "🔸"),
    NONE("Unarmed", 0, "✋");

    private final String displayName;
    private final int damageValue;
    private final String icon;

    WeaponType(String displayName, int damageValue, String icon) {
        this.displayName = displayName;
        this.damageValue = damageValue;
        this.icon = icon;
    }

    public String getDisplayName() { return displayName; }
    public int getDamageValue() { return damageValue; }
    public String getIcon() { return icon; }

    @Override
    public String toString() { return icon + " " + displayName; }
}
