package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.enemies.EnemyStats;
import com.deco2800.game.components.enemies.EnemyStatsComponent;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.ColliderComponent;
import com.deco2800.game.physics.HitboxComponent;
import com.deco2800.game.physics.PhysicsComponent;
import com.deco2800.game.physics.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

public class EnemyFactory {
  private static final EnemyStats ghostStats = FileLoader.loadClass(EnemyStats.class, "entityStats/ghost.json");
  private static final EnemyStats ghostKingStats = FileLoader.loadClass(EnemyStats.class, "entityStats/ghostKing.json");

  public static Entity createGhostKing() {
    Entity ghost = createBaseGhost();
    ghost.addComponent(new EnemyStatsComponent(ghostKingStats))
      .addComponent(new TextureRenderComponent("images/ghost_king.png"));

    ghost.getComponent(TextureRenderComponent.class).scaleEntity();
    return ghost;
  }

  public static Entity createGhost() {
    Entity ghost = createBaseGhost();
    ghost.addComponent(new EnemyStatsComponent(ghostStats))
      .addComponent(new TextureRenderComponent("images/ghost_1.png"));

    ghost.getComponent(TextureRenderComponent.class).scaleEntity();
    return ghost;
  }

  private static Entity createBaseGhost() {
    AITaskComponent aiComponent =
      new AITaskComponent().addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    Entity ghost =
      new Entity()
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent())
        .addComponent(aiComponent);

    EntityFactory.setScaledCollider(ghost, 0.9f, 0.4f);
    return ghost;
  }
}
