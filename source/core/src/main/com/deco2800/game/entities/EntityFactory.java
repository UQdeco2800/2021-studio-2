package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.physics.ColliderComponent;
import com.deco2800.game.physics.HitboxComponent;
import com.deco2800.game.physics.PhysicsMovementComponent;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.player.PlayerActionComponent;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsComponent;
import com.deco2800.game.physics.PhysicsComponent.AlignX;
import com.deco2800.game.physics.PhysicsComponent.AlignY;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class EntityFactory {
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    Entity player =
        new Entity()
            .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent())
            .addComponent(new PlayerActionComponent())
            .addComponent(inputComponent);

    setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(0.3f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    return player;
  }

  public static Entity createGhost() {
    AITaskComponent aiComponent =
        new AITaskComponent().addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    Entity ghost =
        new Entity()
            .addComponent(new TextureRenderComponent("images/ghost_1.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent())
            .addComponent(aiComponent);

    setScaledCollider(ghost, 0.9f, 0.4f);
    ghost.getComponent(TextureRenderComponent.class).scaleEntity();
    return ghost;
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

  // Set the collider to the base of the entity, scaled relative to the entity size.
  private static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
        .getComponent(ColliderComponent.class)
        .setAsBoxAligned(boundingBox, AlignX.Center, AlignY.Bottom);
  }
}
