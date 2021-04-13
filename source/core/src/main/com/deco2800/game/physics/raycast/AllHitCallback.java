package com.deco2800.game.physics.raycast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class AllHitCallback implements RayCastCallback {
  public short layerMask = ~0;
  public RaycastHit hit;
  public boolean didHit;

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    return 0;
  }
}
