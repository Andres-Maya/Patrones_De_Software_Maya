package com.wargame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a named battalion containing a collection of soldiers.
 */
public class Battalion {

    private final String id;
    private String name;
    private final List<Soldier> soldiers;

    public Battalion(String name) {
        this.id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.name = name;
        this.soldiers = new ArrayList<>();
    }

    public void addSoldier(Soldier soldier) {
        soldiers.add(soldier);
    }

    public void removeSoldier(Soldier soldier) {
        soldiers.remove(soldier);
    }

    public int getTotalCombatPower() {
        return soldiers.stream().mapToInt(Soldier::calculateCombatPower).sum();
    }

    public int getAverageCombatPower() {
        if (soldiers.isEmpty()) return 0;
        return getTotalCombatPower() / soldiers.size();
    }

    public int getEliteCount() {
        return (int) soldiers.stream().filter(Soldier::isElite).count();
    }

    // ─── Getters / Setters ────────────────────────────────────────────────────

    public String getId()                          { return id; }
    public String getName()                        { return name; }
    public void setName(String name)               { this.name = name; }
    public List<Soldier> getSoldiers()             { return Collections.unmodifiableList(soldiers); }
    public int getSize()                           { return soldiers.size(); }

    @Override
    public String toString() {
        return String.format("Battalion [%s] '%s' | Soldiers: %d | Power: %d",
                id, name, soldiers.size(), getTotalCombatPower());
    }
}
