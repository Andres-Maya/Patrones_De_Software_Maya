package com.wargame.enums;

/**
 * Represents all available helmet types for soldiers.
 */
public enum HelmetType {
    TITANIUM_HELMET("Titanium Helmet", 90, 5),
    KEVLAR_HELMET("Kevlar Helmet", 75, 3),
    BALLISTIC_HELMET("Ballistic Helmet", 60, 2),
    STEALTH_HELMET("Stealth Helmet", 50, 4),
    COMBAT_HELMET("Combat Helmet", 40, 1),
    NONE("No Helmet", 0, 0);

    private final String displayName;
    private final int defenseValue;
    private final int weightKg;

    HelmetType(String displayName, int defenseValue, int weightKg) {
        this.displayName = displayName;
        this.defenseValue = defenseValue;
        this.weightKg = weightKg;
    }

    public String getDisplayName() { return displayName; }
    public int getDefenseValue() { return defenseValue; }
    public int getWeightKg() { return weightKg; }

    @Override
    public String toString() { return displayName + " (DEF:" + defenseValue + ")"; }
}
