package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatComponent;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.*;
import com.deco2800.game.rendering.TextureRenderComponent;

public class  NPCFactory {
  private static final NPCConfigs configs = FileLoader.loadClass(NPCConfigs.class, "configs/NPCs.json");

  public static Entity createGhost() {
    Entity ghost = createBaseNPC("images/ghost_1.png");
    BaseEntityConfig config = configs.ghost;
    ghost.addComponent(new CombatComponent(config.health, config.baseAttack, config.specialAttack));
    return ghost;
  }

  public static Entity createGhostKing() {
    Entity ghostKing = createBaseNPC("images/ghost_king.png");
    GhostKingConfig config = configs.ghostKing;
    ghostKing.addComponent(new CombatComponent(config.health, config.baseAttack, config.specialAttack));
    return ghostKing;
  }

  private static Entity createBaseNPC(String textureName) {
    AITaskComponent aiComponent =
      new AITaskComponent().addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    Entity npc =
      new Entity()
        .addComponent(new TextureRenderComponent(textureName))
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent())
        .addComponent(aiComponent);

    npc.getComponent(TextureRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }
}
