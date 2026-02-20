package com.wargame.factory;

import com.wargame.model.Soldier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry that stores named {@link Soldier} prototypes.
 * Acts as the "template library" for the Prototype pattern.
 *
 * <p>Any soldier stored here can be cloned on demand without rebuilding from scratch.</p>
 */
public class SoldierRegistry {

    private final Map<String, Soldier> prototypes = new LinkedHashMap<>();

    /**
     * Registers a soldier as a reusable prototype template.
     *
     * @param key    unique identifier for this template
     * @param soldier the prototype soldier to store
     */
    public void registerPrototype(String key, Soldier soldier) {
        prototypes.put(key, soldier);
    }

    /**
     * Removes a prototype from the registry.
     *
     * @param key identifier of the template to remove
     */
    public void removePrototype(String key) {
        prototypes.remove(key);
    }

    /**
     * Clones the prototype identified by {@code key}.
     *
     * @param key the prototype key
     * @return a fresh deep clone of the stored soldier
     * @throws IllegalArgumentException if no prototype is found for the key
     */
    public Soldier clonePrototype(String key) {
        Soldier prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype found for key: " + key);
        }
        return prototype.clone();
    }

    /**
     * Checks whether a prototype exists for the given key.
     */
    public boolean hasPrototype(String key) {
        return prototypes.containsKey(key);
    }

    public Map<String, Soldier> getAllPrototypes() {
        return Collections.unmodifiableMap(prototypes);
    }

    public int getPrototypeCount() {
        return prototypes.size();
    }
}
