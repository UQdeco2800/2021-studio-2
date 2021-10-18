package com.deco2800.game.entities.configs;

/**
 * Defines the properties stored in arrow config files to be loaded by the Weapon Factory.
 */
public class BaseArrowConfig {
    private BaseArrowConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final int HEALTH = 10000;
}
