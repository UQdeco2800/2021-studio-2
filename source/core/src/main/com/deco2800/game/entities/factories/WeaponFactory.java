package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.tasks.ProjectileMovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseArrowConfig;
import com.deco2800.game.entities.configs.FastArrowConfig;
import com.deco2800.game.entities.configs.TrackingArrowConfig;
import com.deco2800.game.entities.configs.WeaponConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * Factory to create non-playable character weapon entities with predefined components.
 */
public class WeaponFactory {
    private static final WeaponConfigs configs =
            FileLoader.readClass(WeaponConfigs.class, "configs/Weapons.json");

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
            Sound arrowEffect = ServiceLocator.getResourceService().getAsset("sounds/arrow_shoot.mp3", Sound.class);
            arrowEffect.play();
        }
    }

    public static Entity createNormalArrow(Vector2 targetLoc, float angle) {
        Entity normalArrow = createBaseArrow();
        normalArrow.setEntityType("arrow");
        BaseArrowConfig config = configs.baseArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(targetLoc, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset("images/arrow_normal.png", Texture.class));
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

    public static Entity createTrackingArrow(Entity targetEntity, float angle) {
        Entity normalArrow = createBaseArrow();
        TrackingArrowConfig config = configs.trackingArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(targetEntity, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset("images/arrow_normal.png", Texture.class));
        normalArrow
                .addComponent(new TextureRenderComponent(sprite))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(aiComponent);
        Vector2 scale = new Vector2(sprite.getWidth() / 40f, sprite.getHeight() / 40f);
        normalArrow.setScale(scale);
        normalArrow.setAngle(angle);

        shootingSound("trackingArrow");
        return normalArrow;
    }

    public static Entity createFastArrow(Vector2 targetLoc, float angle) {
        Entity normalArrow = createBaseArrow();
        FastArrowConfig config = configs.fastArrow;
        ProjectileMovementTask movementTask = new ProjectileMovementTask(targetLoc, new Vector2(config.speedX, config.speedY));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset("images/arrow_normal.png", Texture.class));
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
                .addComponent(new TouchAttackComponent((short) (PhysicsLayer.OBSTACLE | PhysicsLayer.PLAYER), 1f));
    }

}
