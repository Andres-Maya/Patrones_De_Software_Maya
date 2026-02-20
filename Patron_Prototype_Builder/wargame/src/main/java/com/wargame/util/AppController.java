package com.wargame.util;

import com.wargame.builder.SoldierBuilder;
import com.wargame.enums.*;
import com.wargame.factory.BattalionFactory;
import com.wargame.factory.SoldierRegistry;
import com.wargame.model.Battalion;
import com.wargame.model.Soldier;

import java.util.ArrayList;
import java.util.List;

/**
 * Application controller that wires together the Builder, Prototype, and Factory
 * components and maintains the state of battalions and registries.
 */
public class AppController {

    private final SoldierRegistry registry;
    private final BattalionFactory factory;
    private final List<Battalion> battalions;
    private final List<Soldier> savedSoldiers;

    public AppController() {
        this.registry       = new SoldierRegistry();
        this.factory        = new BattalionFactory(registry);
        this.battalions     = new ArrayList<>();
        this.savedSoldiers  = new ArrayList<>();
        seedDefaultPrototypes();
    }

    // ─── Seeding ──────────────────────────────────────────────────────────────

    /**
     * Pre-populates the registry with three default soldier templates.
     */
    private void seedDefaultPrototypes() {
        Soldier sniper = new SoldierBuilder()
                .withName("Ghost")
                .withRank(Rank.SERGEANT)
                .withHealth(120)
                .withArmor(60)
                .withSpeed(90)
                .withPrimaryWeapon(WeaponType.SNIPER_RIFLE)
                .withSecondaryWeapon(WeaponType.PISTOL)
                .withHelmet(HelmetType.STEALTH_HELMET)
                .withSkill(SkillType.SNIPER_TRAINING)
                .withSkill(SkillType.STEALTH)
                .withFaction("Escuadrón Fantasma")
                .asElite(true)
                .build();

        Soldier heavy = new SoldierBuilder()
                .withName("Tank")
                .withRank(Rank.CORPORAL)
                .withHealth(200)
                .withArmor(150)
                .withSpeed(40)
                .withPrimaryWeapon(WeaponType.MACHINE_GUN)
                .withSecondaryWeapon(WeaponType.SHOTGUN)
                .withHelmet(HelmetType.TITANIUM_HELMET)
                .withSkill(SkillType.SURVIVALIST)
                .withFaction("Muro de Hierro")
                .asElite(false)
                .build();

        Soldier medic = new SoldierBuilder()
                .withName("Doc")
                .withRank(Rank.LIEUTENANT)
                .withHealth(100)
                .withArmor(50)
                .withSpeed(80)
                .withPrimaryWeapon(WeaponType.PISTOL)
                .withSecondaryWeapon(WeaponType.NONE)
                .withHelmet(HelmetType.KEVLAR_HELMET)
                .withSkill(SkillType.MEDIC)
                .withSkill(SkillType.SURVIVALIST)
                .withFaction("Apoyo de Campo")
                .asElite(false)
                .build();

        registry.registerPrototype("Sniper Elite", sniper);
        registry.registerPrototype("Heavy Infantry", heavy);
        registry.registerPrototype("Combat Medic", medic);
    }

    // ─── Builder Operations ───────────────────────────────────────────────────

    public Soldier buildSoldier(String name, Rank rank, int health, int armor, int speed,
                                 WeaponType primary, WeaponType secondary, HelmetType helmet,
                                 List<SkillType> skills, String faction, boolean isElite) {
        SoldierBuilder builder = new SoldierBuilder()
                .withName(name)
                .withRank(rank)
                .withHealth(health)
                .withArmor(armor)
                .withSpeed(speed)
                .withPrimaryWeapon(primary)
                .withSecondaryWeapon(secondary)
                .withHelmet(helmet)
                .withFaction(faction)
                .asElite(isElite);

        for (SkillType skill : skills) {
            builder.withSkill(skill);
        }

        return builder.build();
    }

    // ─── Soldier Storage ──────────────────────────────────────────────────────

    public void saveSoldier(Soldier soldier) {
        savedSoldiers.add(soldier);
    }

    public void removeSoldier(Soldier soldier) {
        savedSoldiers.remove(soldier);
    }

    // ─── Registry Operations ──────────────────────────────────────────────────

    public void registerAsPrototype(String key, Soldier soldier) {
        registry.registerPrototype(key, soldier);
    }

    public void removePrototype(String key) {
        registry.removePrototype(key);
    }

    public Soldier cloneSoldier(String prototypeKey) {
        return registry.clonePrototype(prototypeKey);
    }

    // ─── Battalion Operations ─────────────────────────────────────────────────

    public Battalion createBattalion(String name, String prototypeKey, int count) {
        Battalion battalion = factory.createBattalion(name, prototypeKey, count);
        battalions.add(battalion);
        return battalion;
    }

    public Battalion createMixedBattalion(String name, String[] keys, int count) {
        Battalion battalion = factory.createMixedBattalion(name, keys, count);
        battalions.add(battalion);
        return battalion;
    }

    public void removeBattalion(Battalion battalion) {
        battalions.remove(battalion);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public SoldierRegistry getRegistry()         { return registry; }
    public List<Battalion> getBattalions()       { return battalions; }
    public List<Soldier> getSavedSoldiers()      { return savedSoldiers; }
}
