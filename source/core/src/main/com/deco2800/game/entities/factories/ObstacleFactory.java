package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.TouchHealComponent;
import com.deco2800.game.components.crate.CrateAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

    private ObstacleFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Creates a tree entity.
     *
     * @return entity
     */
    public static Entity createTree() {
        Entity tree = new Entity()
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
     *
     * @return trap
     */
    public static Entity createPhysicalTrap() {
        Entity trap = new Entity()
                .addComponent(new TextureRenderComponent("images/trap.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new CombatStatsComponent(1000000, 1))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 2));

        trap.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trap.getComponent(TextureRenderComponent.class).scaleEntity();
        trap.scaleHeight(0.3f);
        return trap;
    }

    /**
     * Creates an invisible physics wall.
     *
     * @param width  Wall width in world units
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
     * creates a crate entity that can be destroyed and will have a potion entity inside.
     *
     * @return crate entity
     */
    public static Entity createCrate() {

        AnimationRenderComponent crateAnimator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("crate/crateHitBreak.atlas", TextureAtlas.class));
        crateAnimator.addAnimation("hit", 0.05f); //default playback NORMAL
        crateAnimator.addAnimation("break", 0.05f);
        crateAnimator.addAnimation("default", 1f);
        crateAnimator.startAnimation("default");
        Entity crate = new Entity()
                .addComponent(crateAnimator)
                .addComponent(new CrateAnimationController())
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(30, 0));

        crate.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        crate.getComponent(AnimationRenderComponent.class).scaleEntity();
        return crate;
    }

    /**
     * create a potion entity that the player can walk over and gain health.
     *
     * @return potion entity
     */
    public static Entity createHealthPotion() {
        Entity potion = new Entity()
                .addComponent(new TextureRenderComponent("healthRegen/healthPotion_placeholder.png"))
                .addComponent(new CombatStatsComponent(0, 99999)) //used to know how much health to restore
                //instead of using CombatStatComponent we could change the health given in the TouchHealComponent
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC).setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchHealComponent(PhysicsLayer.PLAYER));
                //physics components is to detect whether the player has stepped onto the potion entity

        potion.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        potion.getComponent(TextureRenderComponent.class).scaleEntity();
        potion.scaleHeight(0.4f);
        return potion;
    }

    /**
    * Creates an anchor entity to be referenced by other entities.
    *
    * @return entity to serve as the base's real world location
    */
    public static Entity createAnchor() {
        Entity anchor = new Entity()
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
}

