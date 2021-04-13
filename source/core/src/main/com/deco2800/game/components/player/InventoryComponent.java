package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  public int gold;

  public InventoryComponent(int gold) {
    setGold(gold);
  }

  public int getGold() {
    return this.gold;
  }

  public Boolean hasGold(int gold) {
    return gold >= this.gold;
  }

  public void setGold(int gold) {
    if (gold >= 0) {
      this.gold = gold;
    } else {
      this.gold = 0;
    }
  }

  public void addGold(int gold) {
    setGold(this.gold + gold);
  }
}
