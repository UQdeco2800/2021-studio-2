package com.deco2800.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.deco2800.game.components.player.PlayerActionComponent;
import com.deco2800.game.components.player.PlayerStats;
import com.deco2800.game.components.player.PlayerStatsComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.ColliderComponent;
import com.deco2800.game.physics.HitboxComponent;
import com.deco2800.game.physics.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerFactory {
  private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);
  private static final PlayerStats playerStats = FileLoader.loadClass(PlayerStats.class, "entityStats/player.json");

  public static Entity createPlayer() {
    InputComponent inputComponent =
      ServiceLocator.getInputService().getInputFactory().createForPlayer();

    Entity player =
      new Entity()
        .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent())
        .addComponent(new PlayerActionComponent())
        .addComponent(new PlayerStatsComponent(playerStats))
        .addComponent(inputComponent);

    EntityFactory.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(0.3f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    return player;
  }
}
