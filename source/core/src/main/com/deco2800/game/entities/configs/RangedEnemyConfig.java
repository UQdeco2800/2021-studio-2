package com.deco2800.game.entities.configs;

/**
 * Defines the properties stored in ranged enemy config files to be loaded by the NPC Factory.
 */
public class RangedEnemyConfig {
    private RangedEnemyConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final int HEALTH = 40;
    public static final int BASE_ATTACK = 0;
}
