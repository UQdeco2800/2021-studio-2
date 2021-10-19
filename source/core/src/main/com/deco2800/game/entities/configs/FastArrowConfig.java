package com.deco2800.game.entities.configs;

/**
 * Defines the properties stored in arrow config files to be loaded by the Weapon Factory.
 */
public class FastArrowConfig {
    private FastArrowConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final int HEALTH = BaseArrowConfig.HEALTH;
    public static final int BASE_ATTACK = 100;
    public static final float SPEED_X = 30f;
    public static final float SPEED_Y = 30f;
}
