package com.deco2800.game.entities.configs;

public class ArrowConfig {
    private ArrowConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final int HEALTH = BaseArrowConfig.HEALTH;
    public static final int BASE_ATTACK = 30;
    public static final float SPEED_X = 5f;
    public static final float SPEED_Y = 5f;
}
