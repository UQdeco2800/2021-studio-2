package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatComponent;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs =
      FileLoader.loadClass(NPCConfigs.class, "configs/NPCs.json");

  public static Entity createGhost(Entity target) {
    Entity ghost = createBaseNPC("images/ghost_1.png", target);
    BaseEntityConfig config = configs.ghost;
    ghost.addComponent(new CombatComponent(config.health, config.baseAttack));
    return ghost;
  }

  public static Entity createGhostKing(Entity target) {
    Entity ghostKing = createBaseNPC("images/ghost_king.png", target);
    GhostKingConfig config = configs.ghostKing;
    ghostKing.addComponent(new CombatComponent(config.health, config.baseAttack));
    return ghostKing;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param textureName texture name
   * @return entity
   */
  private static Entity createBaseNPC(String textureName, Entity target) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));    Entity npc =
        new Entity()
            .addComponent(new TextureRenderComponent(textureName))
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(aiComponent);

    npc.getComponent(TextureRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }
}
