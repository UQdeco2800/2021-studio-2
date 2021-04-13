package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombatComponent extends Component{
  private static final Logger logger = LoggerFactory.getLogger(CombatComponent.class);
  public int health;
  public int baseAttack;
  public int specialAttack;

  public CombatComponent(int health, int baseAttack, int specialAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setSpecialAttack(specialAttack);
  }

  public Boolean isDead() {
    return health == 0;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    if (health >= 0) {
      this.health = health;
    } else {
      this.health = 0;
    }
  }

  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  public int getBaseAttack() {
    return baseAttack;
  }

  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public int getSpecialAttack() {
    return specialAttack;
  }

  public void setSpecialAttack(int attack) {
    if (attack >= 0) {
      this.specialAttack = attack;
    } else {
      logger.error("Can not set special attack to a negative attack value");
    }
  }
}
