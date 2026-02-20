package com.wargame.model;

import com.wargame.enums.HelmetType;
import com.wargame.enums.Rank;
import com.wargame.enums.SkillType;
import com.wargame.enums.WeaponType;
import com.wargame.prototype.Cloneable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a fully configured soldier in the war game.
 * Supports the Prototype pattern via the {@link Cloneable} interface.
 *
 * <p>Instances should be created using {@link com.wargame.builder.SoldierBuilder}.</p>
 */
public class Soldier implements Cloneable<Soldier> {

    private final String id;
    private String name;
    private Rank rank;
    private int health;
    private int armor;
    private int speed;
    private WeaponType primaryWeapon;
    private WeaponType secondaryWeapon;
    private HelmetType helmet;
    private List<SkillType> skills;
    private String faction;
    private boolean isElite;

    /**
     * Private constructor — use {@link com.wargame.builder.SoldierBuilder} to create instances.
     */
    private Soldier() {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.skills = new ArrayList<>();
    }

    /**
     * Factory method used by the builder.
     */
    public static Soldier createEmpty() {
        return new Soldier();
    }

    // ─── Prototype: Deep Clone ────────────────────────────────────────────────

    @Override
    public Soldier clone() {
        Soldier copy = new Soldier();
        copy.name          = this.name + " [Clone]";
        copy.rank          = this.rank;
        copy.health        = this.health;
        copy.armor         = this.armor;
        copy.speed         = this.speed;
        copy.primaryWeapon = this.primaryWeapon;
        copy.secondaryWeapon = this.secondaryWeapon;
        copy.helmet        = this.helmet;
        copy.skills        = new ArrayList<>(this.skills);
        copy.faction       = this.faction;
        copy.isElite       = this.isElite;
        return copy;
    }

    // ─── Computed Stats ───────────────────────────────────────────────────────

    /**
     * Calculates the overall combat power score for this soldier.
     */
    public int calculateCombatPower() {
        int weaponDmg = (primaryWeapon != null ? primaryWeapon.getDamageValue() : 0)
                      + (secondaryWeapon != null ? secondaryWeapon.getDamageValue() / 2 : 0);
        int helmetDef = (helmet != null ? helmet.getDefenseValue() : 0);
        int skillBonus = skills.size() * 10;
        int eliteBonus = isElite ? 50 : 0;
        int rankBonus = (rank != null ? rank.getLevel() * 5 : 0);
        return health + armor + speed + weaponDmg + helmetDef + skillBonus + eliteBonus + rankBonus;
    }

    // ─── Setters (public for builder access across packages) ─────────────────

    public void setName(String name)                         { this.name = name; }
    public void setRank(Rank rank)                           { this.rank = rank; }
    public void setHealth(int health)                        { this.health = health; }
    public void setArmor(int armor)                          { this.armor = armor; }
    public void setSpeed(int speed)                          { this.speed = speed; }
    public void setPrimaryWeapon(WeaponType weapon)          { this.primaryWeapon = weapon; }
    public void setSecondaryWeapon(WeaponType weapon)        { this.secondaryWeapon = weapon; }
    public void setHelmet(HelmetType helmet)                 { this.helmet = helmet; }
    public void addSkill(SkillType skill)                    { this.skills.add(skill); }
    public void setFaction(String faction)                   { this.faction = faction; }
    public void setElite(boolean elite)                      { this.isElite = elite; }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public String getId()                          { return id; }
    public String getName()                        { return name; }
    public Rank getRank()                          { return rank; }
    public int getHealth()                         { return health; }
    public int getArmor()                          { return armor; }
    public int getSpeed()                          { return speed; }
    public WeaponType getPrimaryWeapon()           { return primaryWeapon; }
    public WeaponType getSecondaryWeapon()         { return secondaryWeapon; }
    public HelmetType getHelmet()                  { return helmet; }
    public List<SkillType> getSkills()             { return Collections.unmodifiableList(skills); }
    public String getFaction()                     { return faction; }
    public boolean isElite()                       { return isElite; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s | HP:%d ARM:%d SPD:%d | PWR:%d",
                id, rank != null ? rank.getTitle() : "?", name,
                health, armor, speed, calculateCombatPower());
    }
}
