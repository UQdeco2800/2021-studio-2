package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActionComponent extends Component {
  @Override
  public void create() {
    entity.getEvents().addListener("walk", (Character direction) -> this.walk(direction));
    entity.getEvents().addListener("attack", () -> this.attack());
  }

  /**
   * Placeholder method to move the player in a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Character direction) { // change from string later to use enum?
    System.out.println(String.format("Player walks in direction %c", direction));
  }

  /** Placeholder method to make the player attack. */
  void attack() {
    System.out.println(String.format("Player attacks"));
  }
}
