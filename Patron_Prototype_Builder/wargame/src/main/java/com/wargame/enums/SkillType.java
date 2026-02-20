package com.wargame.enums;

/**
 * Represents all available combat and support skills for soldiers.
 */
public enum SkillType {
    EXPLOSIVES_EXPERT("Explosives Expert", "Can handle grenades and C4", 30),
    MEDIC("Combat Medic", "Can heal wounded allies", 25),
    SNIPER_TRAINING("Sniper Training", "Enhanced long-range accuracy", 20),
    STEALTH("Stealth Operations", "Reduced detection range", 20),
    ENGINEER("Field Engineer", "Can build and destroy structures", 25),
    HACKER("Cyber Hacker", "Can disable enemy electronics", 35),
    COMMANDER("Squad Commander", "Boosts nearby allies' stats", 40),
    SURVIVALIST("Survivalist", "Increased health regeneration", 15);

    private final String displayName;
    private final String description;
    private final int pointCost;

    SkillType(String displayName, String description, int pointCost) {
        this.displayName = displayName;
        this.description = description;
        this.pointCost = pointCost;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getPointCost() { return pointCost; }

    @Override
    public String toString() { return displayName + " (" + pointCost + "pts)"; }
}
