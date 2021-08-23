package com.deco2800.game.entities.configs;

import com.badlogic.gdx.math.Vector2;

/**
 * Defines the properties stored in ghost king config files to be loaded by the NPC Factory.
 */
public class BaseArrowConfig extends BaseEntityConfig {
  public int health = 10000;
  public int baseAttack = 10;
  public float speedX = 2f;
  public float speedY = 2f;
}
