package com.deco2800.game.areas;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  public ForestGameArea(TerrainFactory terrainFactory) {
    super(terrainFactory);
  }

  public void create() {
    // Make terrain
    TerrainComponent terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));

    // Spawn Trees
    // TODO

    // Spawn entities
    Entity defaultSprite =
        new Entity().addComponent(new AnimationRenderComponent(new TextureAtlas("test.atlas")));
    defaultSprite.setPosition(terrain.tileToWorldPosition(1, 4));
    spawnEntity(defaultSprite);

    AnimationRenderComponent animator = defaultSprite.getComponent(AnimationRenderComponent.class);
    animator.addAnimation("chase_down", 0.1f, PlayMode.LOOP);
    animator.startAnimation("chase_down");
  }
}
