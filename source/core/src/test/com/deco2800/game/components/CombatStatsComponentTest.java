package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {
    @Test
    void shouldSetGetHealth() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        assertEquals(100, combat.getHealth());

        combat.setHealth(150);
        assertEquals(100, combat.getHealth()); //shouldn't go above maxHealth

        combat.setHealth(-50);
        assertEquals(0, combat.getHealth());
    }

    @Test
    void shouldChangeMaxHealth() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        assertEquals(100, combat.getHealth());
        assertEquals(100, combat.getMaxHealth());

        combat.setMaxHealth(150);
        assertEquals(100, combat.getHealth());
        assertEquals(150, combat.getMaxHealth());
    }

    @Test
    void shouldSetHealthToMax() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        combat.setMaxHealth(200);
        combat.setHealth(200);
        assertEquals(200, combat.getHealth());
    }

    @Test
    void shouldCheckIsDead() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        assertFalse(combat.isDead());

        combat.setHealth(0);
        assertTrue(combat.isDead());
    }

    @Test
    void shouldAddHealth() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        combat.addHealth(-500);
        assertEquals(0, combat.getHealth());

        combat.addHealth(100);
        combat.addHealth(-20);
        assertEquals(80, combat.getHealth());
    }

    @Test
    void shouldSetGetBaseAttack() {
        CombatStatsComponent combat = new CombatStatsComponent(100, 20);
        assertEquals(20, combat.getBaseAttack());

        combat.setBaseAttack(150);
        assertEquals(150, combat.getBaseAttack());

        combat.setBaseAttack(-50);
        assertEquals(150, combat.getBaseAttack());
    }
}
