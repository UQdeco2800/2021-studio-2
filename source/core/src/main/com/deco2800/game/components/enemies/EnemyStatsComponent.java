package com.deco2800.game.components.enemies;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnemyStatsComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(EnemyStatsComponent.class);
  private final EnemyStats stats;

  public EnemyStatsComponent() {
    this(new EnemyStats());
  }

  public EnemyStatsComponent(EnemyStats stats) {
    this.stats = stats;
  }

  public int getHealth() {
    return stats.health;
  }

  public void setHealth(int health) {
    if (health >= 0) {
      stats.health = health;
    } else {
      stats.health = 0;
    }
  }

  public void addHealth(int health) {
    setHealth(stats.health + health);
  }

  public int getBaseAttack() {
    return stats.baseAttack;
  }

  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      stats.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public int getSpecialAttack() {
    return stats.baseAttack;
  }

  public void setSpecialAttack(int attack) {
    if (attack >= 0) {
      stats.specialAttack = attack;
    } else {
      logger.error("Can not set special attack to a negative attack value");
    }
  }
}
