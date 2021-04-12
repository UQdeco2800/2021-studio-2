package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.physics.ColliderComponent;
import com.deco2800.game.physics.HitboxComponent;
import com.deco2800.game.physics.PhysicsMovementComponent;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.physics.PhysicsComponent;
import com.deco2800.game.physics.PhysicsComponent.AlignX;
import com.deco2800.game.physics.PhysicsComponent.AlignY;
import com.deco2800.game.rendering.TextureRenderComponent;

public class EntityFactory {
  private static final PlayerFactory playerFactory = new PlayerFactory();
  private static final EnemyFactory enemyFactory = new EnemyFactory();

  public static Entity createPlayer() {
    return playerFactory.createPlayer();
  }

  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent());

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }

  public static Entity createGhost() {
    return enemyFactory.createGhost();
  }

  public static Entity createGhostKing() {
    return enemyFactory.createGhostKing();
  }

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
      .getComponent(ColliderComponent.class)
      .setAsBoxAligned(boundingBox, AlignX.Center, AlignY.Bottom);
  }
}
