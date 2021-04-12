package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.enemies.EnemyStats;
import com.deco2800.game.components.enemies.EnemyStatsComponent;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.*;
import com.deco2800.game.rendering.TextureRenderComponent;

public class NPCFactory {
  private static final EnemyStats ghostStats = FileLoader.loadClass(EnemyStats.class, "entityStats/ghost.json");
  private static final EnemyStats ghostKingStats = FileLoader.loadClass(EnemyStats.class, "entityStats/ghostKing.json");

  public static Entity createGhostKing() {
    Entity ghostKing = createBaseNPC("images/ghost_king.png");
    ghostKing.addComponent(new EnemyStatsComponent(ghostKingStats));
    return ghostKing;
  }

  public static Entity createGhost() {
    Entity ghost = createBaseNPC("images/ghost_1.png");
    ghost.addComponent(new EnemyStatsComponent(ghostStats));
    return ghost;
  }

  private static Entity createBaseNPC(String textureName) {
    AITaskComponent aiComponent =
      new AITaskComponent().addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    Entity npc =
      new Entity()
        .addComponent(new TextureRenderComponent(textureName))
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent())
        .addComponent(aiComponent);

    npc.getComponent(TextureRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }
}
