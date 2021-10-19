package com.deco2800.game.entities.configs;

/**
 * Defines the properties stored in the boss elf config files to be loaded by the NPC Factory.
 */
public class ElfBossConfig {
    private ElfBossConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final int HEALTH = 1000;
    public static final int BASE_ATTACK = 0;
}
