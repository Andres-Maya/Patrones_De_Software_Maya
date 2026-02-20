package com.wargame.factory;

import com.wargame.model.Battalion;
import com.wargame.model.Soldier;

/**
 * Smart factory that uses the {@link SoldierRegistry} to mass-produce battalions.
 *
 * <p>Demonstrates the Prototype pattern: once a template soldier exists,
 * the factory clones it N times to fill a battalion instantly.</p>
 */
public class BattalionFactory {

    private final SoldierRegistry registry;

    public BattalionFactory(SoldierRegistry registry) {
        this.registry = registry;
    }

    /**
     * Creates a full battalion by cloning the named prototype {@code count} times.
     *
     * @param battalionName  name of the battalion to create
     * @param prototypeKey   key of the soldier template in the registry
     * @param count          number of soldiers to clone
     * @return a fully staffed {@link Battalion}
     */
    public Battalion createBattalion(String battalionName, String prototypeKey, int count) {
        if (count < 1) throw new IllegalArgumentException("Battalion must have at least 1 soldier.");
        Battalion battalion = new Battalion(battalionName);
        for (int i = 0; i < count; i++) {
            Soldier clone = registry.clonePrototype(prototypeKey);
            battalion.addSoldier(clone);
        }
        return battalion;
    }

    /**
     * Creates a mixed battalion with soldiers from multiple prototypes.
     *
     * @param battalionName name of the battalion
     * @param keys          array of prototype keys to cycle through
     * @param totalCount    total soldiers to add
     * @return a battalion with mixed soldier types
     */
    public Battalion createMixedBattalion(String battalionName, String[] keys, int totalCount) {
        if (keys == null || keys.length == 0) throw new IllegalArgumentException("Must provide at least one prototype key.");
        Battalion battalion = new Battalion(battalionName);
        for (int i = 0; i < totalCount; i++) {
            String key = keys[i % keys.length];
            battalion.addSoldier(registry.clonePrototype(key));
        }
        return battalion;
    }
}
