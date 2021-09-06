package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }

  /**
   * Creates a trap with collation.
   * @return trap
   */
  public static Entity createPhysicalTrap() {
    Entity trap = new Entity()
            .addComponent(new TextureRenderComponent("images/trap.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new CombatStatsComponent(1000000, 10))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new TouchAttackComponent(PhysicsLayer.TRAP, 4));

    trap.getComponent(HitboxComponent.class).setAsBox(new Vector2(0.33f, 0.33f), new Vector2(0.15f, 0.15f));
    trap.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    trap.getComponent(TextureRenderComponent.class).scaleEntity();
    trap.scaleHeight(0.3f);
    return trap;
  }

  public static Entity createNonePhysicalTrap(){
    Entity trap = new Entity()
            .addComponent(new TextureRenderComponent("images/trap.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new CombatStatsComponent(1000000, 10))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0));

    trap.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    trap.getComponent(TextureRenderComponent.class).scaleEntity();
    trap.scaleHeight(0.3f);
    return trap;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  /**
   * Creates an anchor entity to be referenced by other entities.
   *
   * @return entity to serve as the base's real world location
   */
  public static Entity createAnchor() {
    Entity anchor =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    //hitbox allows the anchor to be seen in debug mode but should be removed out of testing.
                    //.addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
                    //collision can be used instead to test how the follower reacts to a moving anchor
                    //.addComponent(new ColliderComponent())
                    // or an obstacle based anchor (cant see through)
                    //.addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    //Uncomment below to render in game
                    .addComponent(new TextureRenderComponent("images/ghost_crown.png"));
    //Stop from moving
    anchor.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    return anchor;
  }
  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
