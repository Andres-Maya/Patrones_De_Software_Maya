package com.wargame.enums;

/**
 * Military ranks available for soldiers in the war game.
 */
public enum Rank {
    PRIVATE("Private", 1),
    CORPORAL("Corporal", 2),
    SERGEANT("Sergeant", 3),
    LIEUTENANT("Lieutenant", 4),
    CAPTAIN("Captain", 5),
    COLONEL("Colonel", 6),
    GENERAL("General", 7);

    private final String title;
    private final int level;

    Rank(String title, int level) {
        this.title = title;
        this.level = level;
    }

    public String getTitle() { return title; }
    public int getLevel() { return level; }

    @Override
    public String toString() { return title + " (Lv." + level + ")"; }
}
