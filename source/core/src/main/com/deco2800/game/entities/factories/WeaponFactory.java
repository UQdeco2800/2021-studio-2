package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;

import com.deco2800.game.components.Touch.TouchTeleportComponent;
import com.deco2800.game.components.npc.ProjectileAnimationController;
import com.deco2800.game.components.Touch.TouchAttackComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.tasks.EntityHoverTask;
import com.deco2800.game.components.tasks.ProjectileMovementTask;
import com.deco2800.game.components.tasks.VortexSpawnTask;
import com.deco2800.game.components.tasks.WeaponDisposeTask;
import com.deco2800.game.components.weapons.Blast;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.LineEntity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * Factory to create non-playable character weapon entities with predefined components.
 */
public class WeaponFactory {
    /**
     * load attribute from weapon json
     */
    private static final WeaponConfigs configs =
            FileLoader.readClass(WeaponConfigs.class, "configs/Weapons.json");
    private static final PlayerConfig stats =
            FileLoader.readClass(PlayerConfig.class, "configs/player.json");

    /**
     * manages the sound to play when constructing the projectile
     *
     * @param projectileType type of projectile
     */
    private static void shootingSound(String projectileType) {
        if (projectileType.contains("Arrow")) {
            Sound arrowEffect = ServiceLocator.getResourceService().getAsset(
                    "sounds/arrow_shoot.mp3", Sound.class);
            arrowEffect.play();
        }
    }

    /**
     * create the normal arrow entity fly toward target
     *
     * @param targetLoc lock target location
     * @param angle     check for target angle from current shooter position to target
     * @return entity arrow
     */
    public static Entity createNormalArrow(Vector2 targetLoc, float angle) {
        Entity normalArrow = createBaseArrow();
        normalArrow.setEntityType("arrow");
        BaseArrowConfig config = configs.baseArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                targetLoc, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask)
                        .addTask(new WeaponDisposeTask(targetLoc,
                                new Vector2(config.speedX, config.speedY), 0.8f));
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        normalArrow
                //.addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        normalArrow.setScale(scale);
        normalArrow.setAngle(angle);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/arrow_broken/arrowBroken.atlas", TextureAtlas.class));
        animator.addAnimation("brokenArrow", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("arrow", 0.1f, Animation.PlayMode.LOOP);

        normalArrow.setScale(normalArrow.getScale().x * 1.5f,
                normalArrow.getScale().y * 5f);
        animator.startAnimation("arrow");

        normalArrow.addComponent(animator);
        normalArrow.addComponent(new ProjectileAnimationController());

        shootingSound("normalArrow");
        return normalArrow;
    }

    public static Entity createMjolnir() {
        Entity mjolnir =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new WeaponHitboxComponent())
                        .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1f));
        return mjolnir;
    }

    /**
     * create tracking arrow entity that can change trajectory
     *
     * @param targetEntity target (player)
     * @param angle        angle from current entity position to target
     * @return entity tracking arrow
     */
    public static Entity createTrackingArrow(Entity targetEntity, float angle) {
        Entity trackingArrow = createBaseArrow();
        trackingArrow.setEntityType("trackingArrow");
        TrackingArrowConfig config = configs.trackingArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                targetEntity, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask)
                        .addTask(new WeaponDisposeTask(targetEntity.getPosition(),
                                new Vector2(config.speedX, config.speedY), 0.8f));
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        trackingArrow
                //.addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        trackingArrow.setScale(scale);
        trackingArrow.setAngle(angle);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/arrow_broken/arrowBroken.atlas", TextureAtlas.class));
        animator.addAnimation("brokenArrow", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("arrow", 0.1f, Animation.PlayMode.LOOP);

        trackingArrow.setScale(trackingArrow.getScale().x * 1.5f,
                trackingArrow.getScale().y * 5f);

        animator.startAnimation("arrow");

        trackingArrow.addComponent(animator);
        trackingArrow.addComponent(new ProjectileAnimationController());

        shootingSound("trackingArrow");
        return trackingArrow;
    }

    /**
     * create tracking arrow entity that can change trajectory
     *
     * @param targetEntity target (player)
     * @param owner        owner to hover at until needed
     * @return entity tracking arrow
     */
    public static Entity createFireBall(Entity targetEntity, Entity owner, Vector2 offset) {
        Entity fireBall = new Entity();
        fireBall.setEntityType("fireBall");
        TrackingArrowConfig config = configs.trackingArrow;

        //add fireball animation.
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/fireball/fireballAnimation.atlas", TextureAtlas.class));
        animator.addAnimation("flying", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("staticFireball", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("hit", 0.3f, Animation.PlayMode.NORMAL);
        animator.startAnimation("staticFireball");

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new EntityHoverTask(
                                owner, 0.1f, 0, offset, 1.5f))
                        .addTask(new ProjectileMovementTask(
                                targetEntity, new Vector2(config.speedX, config.speedY)))
                        .addTask(new WeaponDisposeTask(targetEntity.getPosition(),
                                new Vector2(config.speedX, config.speedY), 0.8f));
        fireBall.data.put("fireBallMovement", false);

        ColliderComponent hitbox = new HitboxComponent().setLayer(PhysicsLayer.IDLEPROJECTILEWEAPON);

        fireBall
                .addComponent(animator)
                .addComponent(new ProjectileAnimationController())
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(hitbox)
                .addComponent(new PlayerActions())
                .addComponent(new TouchAttackComponent(PhysicsLayer.NONE, 1f));
        shootingSound("fireBall");
        hitbox.setScale(0.8f);
        return fireBall;
    }

    /**
     * create fast arrow - unable to dodge
     * line warning before shoot
     *
     * @param targetLoc target position
     * @param angle     check for target angle from current shooter position to target
     * @return fast arrow entity
     */
    public static Entity createFastArrow(Vector2 targetLoc, float angle) {
        Entity normalArrow = createBaseArrow();
        normalArrow.setEntityType("fastArrow");
        FastArrowConfig config = configs.fastArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                targetLoc, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask)
                        .addTask(new WeaponDisposeTask(targetLoc,
                                new Vector2(config.speedX, config.speedY), 0.8f));
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        normalArrow
                //.addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, 0))
                //damage applied when shooting, arrow is decoration
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        normalArrow.setScale(scale);
        normalArrow.setAngle(angle);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/arrow_broken/arrowBroken.atlas", TextureAtlas.class));
        animator.addAnimation("brokenArrow", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("arrow", 0.1f, Animation.PlayMode.LOOP);

        normalArrow.setScale(normalArrow.getScale().x * 1.5f,
                normalArrow.getScale().y * 5f);
        animator.startAnimation("arrow");

        normalArrow.addComponent(animator);
        normalArrow.addComponent(new ProjectileAnimationController());

        shootingSound("fastArrow");
        return normalArrow;
    }

    /**
     * create the vortex for teleportation
     *
     * @param angle        angle to spin the vortex for transition animate
     * @param reverseSpawn downscale the entity
     * @return entity vortex
     */
    public static Entity createVortexEnter(Entity ownerRunner, float angle, boolean reverseSpawn) {
        Entity vortex = new Entity();
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/vortex.png", Texture.class));
        Vector2 scale = new Vector2(sprite.getWidth() / 30f, sprite.getHeight() / 30f);
        VortexSpawnTask vortexSpawn = new VortexSpawnTask(ownerRunner, scale, 2f);
        if (reverseSpawn) {
            vortexSpawn.flipReverse();
        }
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(vortexSpawn);
        CircleShape circle = new CircleShape();
        circle.setRadius(scale.x / 4);
        circle.setPosition(circle.getPosition().add(scale.cpy().scl(0.5f)));
        vortex
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.TELEPORT)
                        .setShape(circle).setRestitution(0))
                .addComponent(new TouchTeleportComponent(PhysicsLayer.PLAYER, PhysicsLayer.TELEPORT))
                .addComponent(aiTaskComponent);
        //vortex.setScale(scale);
        vortex.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        vortex.setAngle(angle);
        vortex.data.put("teleportID", 1);
        return vortex;
    }

    /**
     * create the vortex for teleportation
     *
     * @param angle        angle to spin the vortex for transition animate
     * @param reverseSpawn downscale the entity
     * @return entity vortex
     */
    public static Entity createVortexExit(Entity ownerRunner, float angle, boolean reverseSpawn) {
        Entity vortex = new Entity();
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/vortex.png", Texture.class));
        Vector2 scale = new Vector2(sprite.getWidth() / 30f, sprite.getHeight() / 30f);
        VortexSpawnTask vortexSpawn = new VortexSpawnTask(ownerRunner, scale, 2f);
        if (reverseSpawn) {
            vortexSpawn.flipReverse();
        }
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(vortexSpawn);
        vortex
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(aiTaskComponent);
        //vortex.setScale(scale);
        vortex.setAngle(angle);
        vortex.data.put("teleportID", 2);
        return vortex;
    }

    /**
     * Creates a line entity
     *
     * @param TTL time to live in MS
     */
    public static LineEntity AimingLine(long TTL) {
        LineEntity line = new LineEntity(TTL);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/aiming_line.png", Texture.class));
        sprite.flip(true, false);
        sprite.setAlpha(0.5f);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        line
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new PhysicsComponent());
        line.setScale(scale);
        return line;
    }

    /**
     * Creates a generic Arrow to be used as a base entity by more specific Arrow creation methods.
     *
     * @return entity
     */
    private static Entity createBaseArrow() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILEWEAPON))
                .addComponent(new PlayerActions())
                .addComponent(new TouchAttackComponent((short) (
                        PhysicsLayer.OBSTACLE | PhysicsLayer.PLAYER), 1f));
    }

    /**
     * Makes an energy ball that will move in a straight line and damage enemies
     *
     * @param target the location that the blast will try and reach
     * @return entity
     */
    public static Entity createBlast(Vector2 target) {
        float speed = 8f;
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/blast.png", Texture.class));
        PhysicsMovementComponent movingComponent = new PhysicsMovementComponent();
        movingComponent.setMoving(true);
        movingComponent.setTarget(target);
        movingComponent.setMaxSpeed(new Vector2(speed, speed));
        Entity blast = new Entity()
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new PhysicsComponent())
                .addComponent(movingComponent)
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.MELEEWEAPON))
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                .addComponent(new Blast());
        return blast;
    }

    public WeaponFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}

