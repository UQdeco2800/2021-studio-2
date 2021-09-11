package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
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
    /** load attribute from config */
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    /** throw error */
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

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ZigChaseTask(
                                target, 11, 4f, 4f))
                        .addTask(new AlertableChaseTask(
                                target, 10, 3f, 4f));

        BaseEntityConfig config = configs.ghost;

        ghost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new GhostAnimationController());

        ghost.getComponent(AITaskComponent.class).addTask(
                new AlertableChaseTask(target, 10, 3f, 4f));
        ghost.getComponent(AITaskComponent.class)
                .addTask(new ZigChaseTask(target, 11, 3f, 6f));

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        ghost.addComponent(healthBarComponent);

        ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
        return ghost;
    }

    /**
     * create ghost king entity
     * @param target entity to chase (player)
     * @return ghost king entity
     */
    public static Entity createGhostKing(Entity target) {
        Entity ghostKing = createBaseNPCNoAI();
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

        GhostKingConfig config = configs.ghostKing;

        ghostKing
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());
        ghostKing.setEntityType("AlertCaller");

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        ghostKing.addComponent(healthBarComponent);

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
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AnchoredWanderTask(anchor, anchorSize, 2f))
                        .addTask(new AnchoredChaseTask(
                                target, 3f, 4f, anchor, anchorSize))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSize));
        anchoredGhost.addComponent(aiComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        BaseEntityConfig config = configs.ghost;

        anchoredGhost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        anchoredGhost.addComponent(healthBarComponent);

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
    public static Entity createAnchoredGhost(
            Entity target, Entity anchor, float anchorSizeX, float anchorSizeY) {
        Entity anchoredGhost = createBaseNPCNoAI();
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AnchoredWanderTask(
                                anchor, anchorSizeX, anchorSizeY, 2f))
                        .addTask(new AnchoredChaseTask(
                                target, 3f,
                                4f, anchor, anchorSizeX, anchorSizeY))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSizeX, anchorSizeY));
        anchoredGhost.addComponent(aiComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        BaseEntityConfig config = configs.ghost;

        anchoredGhost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        anchoredGhost.addComponent(healthBarComponent);

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
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new RangedChaseTask(
                                target, 10, 15f, 20f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType("normalArrow");
        shootProjectileTask.setMultishotChance(0.1);
        //shootProjectileTask.setProjectileType("trackingArrow");
        //shootProjectileTask.setMultishotChance(0.5);
        //shootProjectileTask.setProjectileType("fastArrow");
        //shootProjectileTask.setMultishotChance(0);
        aiComponent.addTask(shootProjectileTask);

        AnimationRenderComponent animator =

                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/ghost.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        Entity ghost = createBaseNPCNoAI();
        GhostRangedConfig config = configs.ghostRanged;

        ghost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController())
                .addComponent(aiComponent);
        ghost.setAttackRange(5);
        //ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        ghost.addComponent(healthBarComponent);
        return ghost;
    }

    /**
     * create boss enemy entity
     * @param target enemy to chase (player)
     * @return boss entity
     */
    public static Entity createBossNPC(Entity target) {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(
                                target, 10, 7f, 10f));


        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(
                                "images/bossEnemy.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);

        aiComponent.addTask(new SpawnMinionsTask(target))
                .addTask(new TeleportationTask(target, 2000));

        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        Entity boss = createBaseNPCNoAI();
        BossConfig config = configs.bossConfig;

        boss
                .addComponent(aiComponent)
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());
        boss.setAttackRange(5);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();
        boss.scaleWidth(2);
        boss.scaleHeight(2);
        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        boss.addComponent(healthBarComponent);

        return boss;

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
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.5f));

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }
}
