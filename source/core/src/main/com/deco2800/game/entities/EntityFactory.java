package com.deco2800.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.player.PlayerActionComponent;
import com.deco2800.game.physics.PhysicsComponent;
import com.deco2800.game.physics.PhysicsComponent.AlignX;
import com.deco2800.game.physics.PhysicsComponent.AlignY;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class EntityFactory {
  public static Entity createPlayer() {
    Texture playerTex = new Texture("box_boy_leaf.png");
    TextureRenderComponent renderComponent = new TextureRenderComponent(playerTex);
    Entity player =
        new Entity()
            .addComponent(renderComponent)
            .addComponent(new PhysicsComponent())
            .addComponent(new PlayerActionComponent())
            .addComponent(ServiceLocator.getInputService().getInputFactory().createForPlayer());
    renderComponent.scaleEntity();
    return player;
  }

  public static Entity createTree() {
    Texture treeTex = new Texture("tree.png");
    TextureRenderComponent renderComponent = new TextureRenderComponent(treeTex);
    PhysicsComponent physicsComponent = new PhysicsComponent();

    Entity tree = new Entity()
        .addComponent(renderComponent)
        .addComponent(physicsComponent);
    renderComponent.scaleEntity();
    tree.scaleHeight(2.5f);

    // Set a custom physics box that covers only the base of the tree
    Vector2 boundingBox = new Vector2(tree.getScale().x * 0.5f, tree.getScale().y * 0.25f);
    physicsComponent.setAsBoxAligned(boundingBox, AlignX.Center, AlignY.Bottom);
    physicsComponent.setBodyType(BodyType.StaticBody);

    return tree;
  }
}
