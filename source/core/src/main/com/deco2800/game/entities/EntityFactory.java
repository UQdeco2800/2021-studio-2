package com.deco2800.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
    Texture playerTex =
        ServiceLocator.getResourceService().getAsset("images/box_boy_leaf.png", Texture.class);
    TextureRenderComponent renderComponent = new TextureRenderComponent(playerTex);
    PhysicsComponent physicsComponent = new PhysicsComponent();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    Entity player =
        new Entity()
            .addComponent(renderComponent)
            .addComponent(physicsComponent)
            .addComponent(new PlayerActionComponent())
            .addComponent(inputComponent);

    Vector2 boundingBox = player.getScale().cpy().scl(0.7f, 0.3f);
    physicsComponent.setAsBoxAligned(boundingBox, AlignX.Center, AlignY.Bottom);
    physicsComponent.setDensity(0.3f);
    renderComponent.scaleEntity();
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
            .addComponent(aiComponent);
    ghost.getComponent(TextureRenderComponent.class).scaleEntity();
    return ghost;
  }

  public static Entity createTree() {
    Texture treeTex =
        ServiceLocator.getResourceService().getAsset("images/tree.png", Texture.class);
    TextureRenderComponent renderComponent = new TextureRenderComponent(treeTex);
    PhysicsComponent physicsComponent = new PhysicsComponent();

    Entity tree = new Entity().addComponent(renderComponent).addComponent(physicsComponent);
    renderComponent.scaleEntity();
    tree.scaleHeight(2.5f);

    // Set a custom physics box that covers only the base of the tree
    Vector2 boundingBox = tree.getScale().cpy().scl(0.5f, 0.2f);
    physicsComponent.setAsBoxAligned(boundingBox, AlignX.Center, AlignY.Bottom);
    physicsComponent.setBodyType(BodyType.StaticBody);

    return tree;
  }
}
