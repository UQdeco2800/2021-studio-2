package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.GhostRangedConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;


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
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private NPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Creates a ghost entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createGhost(Entity target) {
        Entity ghost = createBaseNPCNoAI();
        BaseEntityConfig config = configs.ghost;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);
      
      
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ZigChaseTask(target, 11, 4f, 4f, 1f))
                        .addTask(new AlertableChaseTask(target, 10, 3f, 4f));

        ghost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new GhostAnimationController());

        ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
        return ghost;
    }

    public static Entity createGhostKing(Entity target) {
        Entity ghostKing = createBaseNPCNoAI();
        GhostKingConfig config = configs.ghostKing;
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                .addTask(new AlertChaseTask(target, 10, 3f, 4f));
        ghostKing.addComponent(aiTaskComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.LOOP);

        ghostKing
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());
        ghostKing.setEntityType("AlertCaller");

        ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
        return ghostKing;
    }

    /**
     * Creates an anchored ghost entity.
     *
     * @param target     entity to chase
     * @param anchor     base entity to anchor to
     * @param anchorSize how big the base's area
     * @return entity
     */

    public static Entity createAnchoredGhost(Entity target, Entity anchor, float anchorSize) {
        Entity anchoredGhost = createBaseNPCNoAI();
        BaseEntityConfig config = configs.ghost;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AnchoredWanderTask(anchor, anchorSize, 2f))
                        .addTask(new AnchoredChaseTask(target, 3f, 4f, anchor, anchorSize))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSize));
        anchoredGhost.addComponent(aiComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        anchoredGhost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());

        anchoredGhost.getComponent(AnimationRenderComponent.class).scaleEntity();
        return anchoredGhost;
    }

    /**
     * Creates a anchored ghost entity.
     * Anchor ghost only chase the target if the target approach the anchor point
     *
     * @param target      entity to chase
     * @param anchor      base entity to anchor to
     * @param anchorSizeX how big the base's area is in the X axis
     * @param anchorSizeY how big the base's area is in the Y axis
     * @return entity
     */
    public static Entity createAnchoredGhost(Entity target, Entity anchor, float anchorSizeX, float anchorSizeY) {
        Entity anchoredGhost = createBaseNPCNoAI();
        BaseEntityConfig config = configs.ghost;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AnchoredWanderTask(anchor, anchorSizeX, anchorSizeY, 2f))
                        .addTask(new AnchoredChaseTask(target, 3f, 4f, anchor, anchorSizeX, anchorSizeY))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSizeX, anchorSizeY));
        anchoredGhost.addComponent(aiComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        anchoredGhost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());

        anchoredGhost.getComponent(AnimationRenderComponent.class).scaleEntity();
        return anchoredGhost;
    }

    /**
     * Creates a ranged ghost entity.
     * Ghost that shoot arrow at target
     * It will retreat if the target is approach in certain range
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createRangedGhost(Entity target) {
        Entity ghost = createBaseNPCNoAI();
        GhostRangedConfig config = configs.ghostRanged;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new RangedChaseTask(target, 10, 15f, 20f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType("normalArrow");
        shootProjectileTask.setMultishotChance(0.1);
        //shootProjectileTask.setProjectileType("trackingArrow");
        //shootProjectileTask.setMultishotChance(0.05);
        //shootProjectileTask.setProjectileType("fastArrow");
        //shootProjectileTask.setMultishotChance(0);
        aiComponent.addTask(shootProjectileTask);

        AnimationRenderComponent animator =

                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        ghost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController())
                .addComponent(aiComponent);


        TextureAtlas healthAtlas =
                ServiceLocator.getResourceService().getAsset("images/health_bar.atlas", TextureAtlas.class);
        HealthBarComponent health = new HealthBarComponent(healthAtlas);

        health.addAnimation("10",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("9",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("8",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("7",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("6",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("5",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("4",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("3",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("2",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("1",0.1f,Animation.PlayMode.NORMAL);
        health.addAnimation("0",0.1f,Animation.PlayMode.NORMAL);
        ghost.addComponent(health);

        ghost.setAttackRange(5);
        ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
        return ghost;
    }

    /**
     * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
     *
     * @return entity
     */
    private static Entity createBaseNPC(Entity target) {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 3f, 4f));
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 2.5f))
                        .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }

    /**
     * Creates a generic NPC, with no ai,
     * to be used as a base entity by more specific NPC creation methods.
     *
     * @return entity
     */
    private static Entity createBaseNPCNoAI() {
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 2.5f));

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }
}
