package com.deco2800.game.components.weapons.projectiles;

public class BlastStats {
    private BlastStats() {
        throw new IllegalStateException("Utility class");
    }

    public static final long PROJECTILE_LIFESPAN = 1000L;
    public static final int ATTACK_POWER = 10;
    public static final int KNOCKBACK = 10;
}
