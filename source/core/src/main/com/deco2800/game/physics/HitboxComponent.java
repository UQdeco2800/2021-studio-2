package com.deco2800.game.physics;

public class HitboxComponent extends ColliderComponent {
  @Override
  public void create() {
    setSensor(true);
    super.create();
  }
}
