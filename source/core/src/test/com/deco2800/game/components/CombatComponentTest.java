package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
public class CombatComponentTest {

  @Test
  void shouldSetGetHealth() {
    CombatComponent combat = new CombatComponent(100, 20);
    assertEquals(combat.getHealth(), 100);

    combat.setHealth(150);
    assertEquals(combat.getHealth(), 150);

    combat.setHealth(-50);
    assertEquals(combat.getHealth(), 0);
  }

  @Test
  void shouldCheckIsDead() {
    CombatComponent combat = new CombatComponent(100, 20);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatComponent combat = new CombatComponent(100, 20);
    combat.addHealth(-500);
    assertEquals(combat.getHealth(), 0);

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(combat.getHealth(), 80);
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatComponent combat = new CombatComponent(100, 20);
    assertEquals(combat.getBaseAttack(), 20);

    combat.setBaseAttack(150);
    assertEquals(combat.getBaseAttack(), 150);

    combat.setBaseAttack(-50);
    assertEquals(combat.getBaseAttack(), 150);
  }
}
