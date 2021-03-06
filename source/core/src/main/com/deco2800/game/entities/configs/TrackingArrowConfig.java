package com.deco2800.game.entities.configs;

/**
 * Defines the properties stored in arrow config files to be loaded by the Weapon Factory.
 */
public class TrackingArrowConfig {
    private TrackingArrowConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final int HEALTH = BaseArrowConfig.HEALTH;
    public static final int BASE_ATTACK = 60;
    public static final float SPEED_X = 2f;
    public static final float SPEED_Y = 2f;
}
