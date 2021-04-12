package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.physics.*;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.physics.PhysicsComponent.AlignX;
import com.deco2800.game.physics.PhysicsComponent.AlignY;
import com.deco2800.game.rendering.TextureRenderComponent;

public class EntityFactory {
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent());

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }
}
