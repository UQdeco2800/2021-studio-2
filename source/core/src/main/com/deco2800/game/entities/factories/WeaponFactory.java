package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.tasks.EntityHoverTask;
import com.deco2800.game.components.tasks.ProjectileMovementTask;
import com.deco2800.game.components.tasks.VortexSpawnTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.LineEntity;
import com.deco2800.game.entities.configs.BaseArrowConfig;
import com.deco2800.game.entities.configs.FastArrowConfig;
import com.deco2800.game.entities.configs.TrackingArrowConfig;
import com.deco2800.game.entities.configs.WeaponConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
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

    /**
     * throw error
     */
    private WeaponFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

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
        /*ProjectileMovementTask movementTask = new ProjectileMovementTask(
                targetEntity, new Vector2(config.speedX, config.speedY));*/
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new EntityHoverTask(owner, 0.1f, 0, offset, 1.5f))
                        .addTask(new ProjectileMovementTask(targetEntity, new Vector2(config.speedX, config.speedY)));
        fireBall.data.put("fireBallMovement", false);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/arrow_normal.png", Texture.class));
        fireBall
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILEWEAPON))
                .addComponent(new PlayerActions())
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1f));
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        fireBall.setScale(scale);
        shootingSound("fireBall");
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
    public static Entity createVortex(float angle, boolean reverseSpawn) {
        // if the player touch the vortex - will receive the damage (instant death)
        Entity vortex = new Entity();
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/vortex.png", Texture.class));
        Vector2 scale = new Vector2(sprite.getWidth() / 30f, sprite.getHeight() / 30f);
        VortexSpawnTask vortexSpawn = new VortexSpawnTask(scale, 2f);
        if (reverseSpawn) {
            vortexSpawn.flipReverse();
        }
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(vortexSpawn);
        vortex
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent(sprite))
                //.addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(1000, 0))
                .addComponent(aiTaskComponent)
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f));
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
}
