package com.deco2800.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class MeleeHellWarriorConfig {
    private MeleeHellWarriorConfig() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * health attribute
     */
    public static final int HEALTH = 200;
    /**
     * attack attribute
     */
    public static final int BASE_ATTACK = 30;
}
