package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.crate.CrateAnimationController;
import com.deco2800.game.components.crate.TransformBarrelComponent;
import com.deco2800.game.components.touch.TeleportComponent;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.components.touch.TouchHealComponent;
import com.deco2800.game.components.touch.TouchWin;
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
     * Creates a non-physical entity with no collision
     *
     * @return entity
     */
    public static Entity createObstacle(String tileRef) {
        Entity obstacle =
                new Entity()
                        .addComponent(new TextureRenderComponent(tileRef));
        obstacle.getComponent(TextureRenderComponent.class).scaleEntity();
        obstacle.scaleHeight(0.5f);
        return obstacle;
    }


    /**
     * Creates a trap with collision resizable.
     *
     * @return trap
     */
    public static Entity createRSPhysicalTrap(float width, float height) {
        Entity trap = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new CombatStatsComponent(1000000, 10))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.TRAP, 4));
        trap.setScale(width, height);
        return trap;
    }

    /**
     * Creates a trap with no collision resizable.
     *
     * @return trap
     */
    public static Entity createRSNonePhysicalTrap(float width, float height) {
        Entity trap = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new CombatStatsComponent(1000000, 70))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.TRAP, 0));
        trap.setScale(width, height);
        return trap;
    }

    public static Entity createNonePhysicalTrap() {
        Entity trap = new Entity()
                .addComponent(new TextureRenderComponent("images/trap.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(1000000, 10))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.TRAP, 0));

        trap.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trap.getComponent(TextureRenderComponent.class).scaleEntity();
        trap.scaleHeight(0.3f);
        return trap;
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
                .addComponent(new TouchAttackComponent(PhysicsLayer.TRAP, 1));

        trap.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trap.getComponent(TextureRenderComponent.class).scaleEntity();
        trap.scaleHeight(0.3f);
        return trap;
    }


    public static Entity createTeleport() {

        Entity teleport = new Entity()
                .addComponent(new TextureRenderComponent("Assets/gametile-127.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(1000000, 10))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TeleportComponent(PhysicsLayer.TRAP));

        teleport.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        teleport.getComponent(TextureRenderComponent.class).scaleEntity();
        teleport.scaleHeight(0.3f);
        return teleport;
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
     * creates a crate obstacle that can be destroyed and will transform into a health potion
     *
     * @return the transforming crate health potion entity
     */
    public static Entity createHealthCrate() {
        AnimationRenderComponent crateAnimator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("crate/crateHitBreak.atlas",
                        TextureAtlas.class));

        crateAnimator.addAnimation("barrelHit", 0.06f); //default playback NORMAL
        crateAnimator.addAnimation("barrelDeath", 0.04f); //break contains the transform animation
        crateAnimator.addAnimation("default", 1f);
        crateAnimator.startAnimation("default");

        Entity crate = new Entity()
                .addComponent(crateAnimator)
                .addComponent(new CrateAnimationController())
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(30, 0))
                .addComponent(new TransformBarrelComponent())
                .addComponent(new TouchHealComponent(PhysicsLayer.PLAYER));

        crate.getComponent(TouchHealComponent.class).setEnabled(false);
        crate.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        crate.getComponent(AnimationRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(crate, 0.6f, 0.3f);
        return crate;
    }

    public static Entity winCondition() {
        AnimationRenderComponent win2 = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("end/portal.atlas",
                        TextureAtlas.class));
        win2.addAnimation("spawn", 0.2f, Animation.PlayMode.NORMAL);
        win2.addAnimation("rotate", 0.3f, Animation.PlayMode.LOOP);
        win2.setAnimationScale(3f);
        win2.startAnimation("spawn");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                win2.startAnimation("rotate");
            }
        }, 1f);


        Entity win = new Entity()
                .addComponent(win2)
                .addComponent(new TouchWin(PhysicsLayer.PLAYER))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
        win.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        return win;
    }

    /**
     * Creates an anchor entity to be referenced by other entities.
     *
     * @return entity to serve as the base's real world location
     */
    public static Entity createAnchor() {
        Entity anchor = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent("images/crown.png"));
        //Stop from moving
        anchor.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        return anchor;
    }
}
