package com.wargame.builder;

import com.wargame.enums.HelmetType;
import com.wargame.enums.Rank;
import com.wargame.enums.SkillType;
import com.wargame.enums.WeaponType;
import com.wargame.model.Soldier;

public class SoldierBuilder {

    private String name          = "Unknown";
    private Rank rank            = Rank.PRIVATE;
    private int health           = 100;
    private int armor            = 50;
    private int speed            = 60;
    private WeaponType primary   = WeaponType.ASSAULT_RIFLE;
    private WeaponType secondary = WeaponType.PISTOL;
    private HelmetType helmet    = HelmetType.COMBAT_HELMET;
    private final java.util.List<SkillType> skills = new java.util.ArrayList<>();
    private String faction       = "Unassigned";
    private boolean isElite      = false;

    public SoldierBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SoldierBuilder withRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    public SoldierBuilder withHealth(int health) {
        if (health < 1 || health > 500) throw new IllegalArgumentException("Health must be 1–500");
        this.health = health;
        return this;
    }

    public SoldierBuilder withArmor(int armor) {
        if (armor < 0 || armor > 200) throw new IllegalArgumentException("Armor must be 0–200");
        this.armor = armor;
        return this;
    }

    public SoldierBuilder withSpeed(int speed) {
        if (speed < 1 || speed > 150) throw new IllegalArgumentException("Speed must be 1–150");
        this.speed = speed;
        return this;
    }

    public SoldierBuilder withPrimaryWeapon(WeaponType weapon) {
        this.primary = weapon;
        return this;
    }

    public SoldierBuilder withSecondaryWeapon(WeaponType weapon) {
        this.secondary = weapon;
        return this;
    }

    public SoldierBuilder withHelmet(HelmetType helmet) {
        this.helmet = helmet;
        return this;
    }

    public SoldierBuilder withSkill(SkillType skill) {
        if (!skills.contains(skill)) skills.add(skill);
        return this;
    }

    public SoldierBuilder withFaction(String faction) {
        this.faction = faction;
        return this;
    }

    public SoldierBuilder asElite(boolean elite) {
        this.isElite = elite;
        return this;
    }

    public Soldier build() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Soldier must have a name.");
        }

        Soldier soldier = Soldier.createEmpty();
        soldier.setName(name);
        soldier.setRank(rank);
        soldier.setHealth(health);
        soldier.setArmor(armor);
        soldier.setSpeed(speed);
        soldier.setPrimaryWeapon(primary);
        soldier.setSecondaryWeapon(secondary);
        soldier.setHelmet(helmet);
        skills.forEach(soldier::addSkill);
        soldier.setFaction(faction);
        soldier.setElite(isElite);
        return soldier;
    }

    public SoldierBuilder reset() {
        return new SoldierBuilder();
    }
}
