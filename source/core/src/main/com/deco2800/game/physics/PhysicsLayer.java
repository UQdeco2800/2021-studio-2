package com.deco2800.game.physics;

public class PhysicsLayer {
    public static final short NONE = 0;
    public static final short DEFAULT = (1);
    public static final short PLAYER = (1 << 1);
    // Terrain obstacle, e.g. trees
    public static final short OBSTACLE = (1 << 2);
    // NPC (Non-Playable Character) colliders
    public static final short NPC = (1 << 3);
    // Weapon colliders
    public static final short MELEEWEAPON = (1 << 4);
    public static final short PROJECTILEWEAPON = (1 << 5);
    public static final short IDLEPROJECTILEWEAPON = (1 << 6);
    public static final short TRAP = (1 << 7);
    public static final short TELEPORT = (1 << 8);

    public static final short ALL = ~0;


    private PhysicsLayer() {
        throw new IllegalStateException("Instantiating static util class");
    }

    public static boolean notContains(short filterBits, short layer) {
        return (filterBits & layer) == 0;
    }
}
