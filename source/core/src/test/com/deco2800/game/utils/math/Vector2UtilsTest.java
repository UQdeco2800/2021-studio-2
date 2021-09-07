package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.weapons.MeleeWeapon;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class Vector2UtilsTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    ServiceLocator.registerTimeSource(new GameTime());
  }

  @Test
  void shouldToDirection() {
    Vector2 vectorUp = new Vector2(0f, -100f);
    Vector2 vectorRight = new Vector2(100f, 0f);
    Vector2 vectorDown = new Vector2(0f, 100f);
    Vector2 vectorLeft = new Vector2(-100f, 0f);
    assertEquals(Vector2Utils.UP, Vector2Utils.toDirection(vectorUp));
    assertEquals(Vector2Utils.RIGHT, Vector2Utils.toDirection(vectorRight));
    assertEquals(Vector2Utils.DOWN, Vector2Utils.toDirection(vectorDown));
    assertEquals(Vector2Utils.LEFT, Vector2Utils.toDirection(vectorLeft));
  }

  @Test
  void shouldToWeaponDirection() {
    assertEquals(MeleeWeapon.UP, Vector2Utils.toWeaponDirection(Vector2Utils.UP));
    assertEquals(MeleeWeapon.DOWN, Vector2Utils.toWeaponDirection(Vector2Utils.DOWN));
    assertEquals(MeleeWeapon.LEFT, Vector2Utils.toWeaponDirection(Vector2Utils.LEFT));
    assertEquals(MeleeWeapon.RIGHT, Vector2Utils.toWeaponDirection(Vector2Utils.RIGHT));
  }
}
