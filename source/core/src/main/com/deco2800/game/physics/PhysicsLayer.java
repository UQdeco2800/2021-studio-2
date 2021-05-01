package com.deco2800.game.physics;

public class PhysicsLayer {
  public static final short None = 0;
  public static final short Default = (1 << 0);
  public static final short Player = (1 << 1);
  // Terrain obstacle, e.g. trees
  public static final short Obstacle = (1 << 2);
  // NPC (Non-Playable Character) colliders
  public static final short NPC = (1 << 3);
  public static final short All = ~0;

  public static boolean contains(short filterBits, short layer) {
    return (filterBits & layer) != 0;
  }
}
