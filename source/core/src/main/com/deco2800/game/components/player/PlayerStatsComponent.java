package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.PlayerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerStatsComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(PlayerStatsComponent.class);
  private final PlayerStats stats;

  public PlayerStatsComponent() {
    this(new PlayerStats());
  }

  public PlayerStatsComponent(PlayerStats stats) {
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

  public int getGold() {
    return stats.gold;
  }

  public Boolean hasGold(int gold) {
    return gold >= stats.gold;
  }

  public void setGold(int gold) {
    if (gold >= 0) {
      stats.gold = gold;
    } else {
      stats.gold = 0;
    }
  }

  public void addGold(int gold) {
    setGold(stats.gold + gold);
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
}
