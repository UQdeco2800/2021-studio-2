package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseArrowConfig;
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
  private static final WeaponConfigs configs =
      FileLoader.readClass(WeaponConfigs.class, "configs/Weapons.json");

  public static Entity createNormalArrow(Vector2 targetLoc, float rotation) {
    Entity normalArrow = createBaseArrow();
    BaseArrowConfig config = configs.baseArrow;
    ProjectileMovementTask movementTask = new ProjectileMovementTask(targetLoc, new Vector2(config.speedX, config.speedY));
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(movementTask);
    Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset("images/arrow_normal.png", Texture.class));
    normalArrow
        .addComponent(new TextureRenderComponent("images/arrow_normal.png"))

        .addComponent(new TextureRenderComponent(sprite, rotation))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(aiComponent);
    Vector2 scale = new Vector2(0.86f,0.22f);
    //scale = new Vector2(sprite.getRegionWidth()/50f,sprite.getRegionHeight()/50f);
    scale = new Vector2(sprite.getWidth()/40f,sprite.getHeight()/40f);
    scale.rotateAroundDeg(new Vector2(0,0),  rotation);
    normalArrow.setScale(scale);
    return normalArrow;
  }

  /**
   * Creates a generic Arrow to be used as a base entity by more specific Arrow creation methods.
   *
   * @return entity
   */
  private static Entity createBaseArrow() {
    Entity arrow =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            //.addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILEWEAPON))
            .addComponent(new PlayerActions())
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1f));

    return arrow;
  }

  private WeaponFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
