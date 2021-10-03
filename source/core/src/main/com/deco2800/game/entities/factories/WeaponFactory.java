package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.tasks.EntityHoverTask;
import com.deco2800.game.components.tasks.ProjectileMovementTask;
import com.deco2800.game.components.tasks.VortexSpawnTask;
import com.deco2800.game.components.weapons.Blast;
import com.deco2800.game.components.weapons.Hammer;
import com.deco2800.game.components.weapons.HammerProjectile;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.entities.LineEntity;
import com.deco2800.game.entities.configs.BaseArrowConfig;
import com.deco2800.game.entities.configs.FastArrowConfig;
import com.deco2800.game.entities.configs.TrackingArrowConfig;
import com.deco2800.game.entities.configs.WeaponConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
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
                        .addTask(movementTask);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        normalArrow
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        normalArrow.setScale(scale);
        normalArrow.setAngle(angle);

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
        TrackingArrowConfig config = configs.trackingArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                targetEntity, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        trackingArrow
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        trackingArrow.setScale(scale);
        trackingArrow.setAngle(angle);

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
        TrackingArrowConfig config = configs.trackingArrow;

        //add fireball animation.
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/fireball/fireballAinmation.atlas", TextureAtlas.class));
        animator.addAnimation("flying", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("staticFireball", 0.1f, Animation.PlayMode.LOOP);
        animator.startAnimation("staticFireball");

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new EntityHoverTask(owner, 0.1f, 0, offset, 1.5f))
                        .addTask(new ProjectileMovementTask(targetEntity, new Vector2(config.speedX, config.speedY)));
        fireBall.data.put("fireBallMovement", false);

        ColliderComponent hitbox = new HitboxComponent().setLayer(PhysicsLayer.PROJECTILEWEAPON);

        fireBall
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(hitbox)
                .addComponent(new PlayerActions())
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1f));
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
        FastArrowConfig config = configs.fastArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                targetLoc, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        normalArrow
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, 0)) //damage applied when shooting, arrow is decorational
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        normalArrow.setScale(scale);
        normalArrow.setAngle(angle);

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
    public static Entity createVortex(Entity ownerRunner, float angle, boolean reverseSpawn) {
        // if the player touch the vortex - will receive the damage (instant death)
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
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILEWEAPON))
                .addComponent(new CombatStatsComponent(1000, 0))
                .addComponent(aiTaskComponent);
        //vortex.setScale(scale);
        vortex.setAngle(angle);
        return vortex;
    }

    /**
     * Creates a line entity
     *
     * @param TTL time to live in MS
     */
    public static LineEntity AimingLine(long TTL) {
        LineEntity line = new LineEntity(TTL);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset("images/aiming_line.png", Texture.class));
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
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset("images/blast.png", Texture.class));
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

    /**
     * Makes a hammer projectile that will move towards a target, and then
     * back to the owner entity, when called.
     * @param target the location that the blast will try and reach
     * @param targetLayer the physics layer mjolnir can damage
     * @param owner - the hammer weapon component that controls the projectile.
     * @return entity
     */
    public static Entity createMjolnir(short targetLayer, Vector2 target, Hammer owner) {
        float speed = 10f;

        PhysicsMovementComponent movingComponent = new PhysicsMovementComponent();
        movingComponent.setMoving(true);
        movingComponent.setTarget(target);
        movingComponent.setMaxSpeed(new Vector2(speed, speed));
        Entity mjolnir = new Entity()
                //.addComponent(new AnimationRenderComponent())
                .addComponent(new TextureRenderComponent("images/hammer.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(movingComponent)
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.MELEEWEAPON))
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                .addComponent(new HammerProjectile(targetLayer, owner));
        return mjolnir;
    }
    public WeaponFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}

