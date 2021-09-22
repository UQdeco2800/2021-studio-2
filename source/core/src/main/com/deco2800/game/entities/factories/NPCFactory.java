package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.BossOverlayComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.ElfAnimationController;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.ElfBossConfig;
import com.deco2800.game.entities.configs.MeleeEnemyConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.entities.configs.RangedEnemyConfig;
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
    /**
     * load attribute from config
     */
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    /**
     * throw error
     */
    private NPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Creates a melee elf entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createMeleeElf(Entity target) {
        Entity elf = createBaseNPCNoAI();
        MeleeEnemyConfig config = configs.elfMelee;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
        animator.addAnimation("moveLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveDown", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("frontDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("leftDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("rightDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("backDeath", 0.2f, Animation.PlayMode.NORMAL);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new PauseTask())
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ZigChaseTask(
                                target, 11, 4f, 4f, 1))
                        .addTask(new AlertableChaseTask(
                                target, 10, 3f, 4f))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        elf
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new ElfAnimationController());

        elf.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        elf.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        elf.addComponent(healthBarComponent);

        elf.getComponent(AnimationRenderComponent.class).scaleEntity();
        elf.setScale(0.6f, 1f);
        elf.setEntityType("melee");
        PhysicsUtils.setScaledCollider(elf, 0.9f, 0.2f);
        return elf;
    }

    public static Entity createElfGuard(Entity target) {
        Entity elfGuard = createBaseNPCNoAI();
        ElfBossConfig config = configs.elfBoss;
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new PauseTask())
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                .addTask(new AlertChaseTask(target, 10, 3f, 4f))
                .addTask(new DeathPauseTask(
                        target, 0, 100, 100, 1.5f));
        elfGuard.addComponent(aiTaskComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/guardElf.atlas", TextureAtlas.class));
        animator.addAnimation("moveLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveDown", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("backDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("frontDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("rightDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("leftDeath", 0.2f, Animation.PlayMode.NORMAL);

        elfGuard
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new ElfAnimationController());
        elfGuard.setEntityType("AlertCaller");

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        elfGuard.addComponent(healthBarComponent);

        elfGuard.getComponent(AnimationRenderComponent.class).scaleEntity();
        elfGuard.setScale(0.6f, 1f);
        elfGuard.setEntityType("melee");
        PhysicsUtils.setScaledCollider(elfGuard, 0.9f, 0.2f);
        return elfGuard;
    }

    /**
     * Creates an anchored elf entity.
     *
     * @param target     entity to chase
     * @param anchor     base entity to anchor to
     * @param anchorSize how big the base's area
     * @return entity
     */
    public static Entity createAnchoredElf(Entity target, Entity anchor, float anchorSize) {
        Entity anchoredElf = createBaseNPCNoAI();
        MeleeEnemyConfig config = configs.elfMelee;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new PauseTask())
                        .addTask(new AnchoredWanderTask(anchor, anchorSize, 2f))
                        .addTask(new AnchoredChaseTask(
                                target, 3f, 4f, anchor, anchorSize))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSize))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        anchoredElf.addComponent(aiComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
        animator.addAnimation("moveLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveDown", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("frontDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("leftDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("rightDeath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("backDeath", 0.2f, Animation.PlayMode.NORMAL);


        anchoredElf
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new ElfAnimationController());

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        anchoredElf.addComponent(healthBarComponent);

        anchoredElf.getComponent(AnimationRenderComponent.class).scaleEntity();
        anchoredElf.setScale(0.6f, 1f);
        anchoredElf.setEntityType("melee");
        PhysicsUtils.setScaledCollider(anchoredElf, 0.9f, 0.2f);
        return anchoredElf;
    }

    /**
     * Creates a anchored elf entity.
     * Anchor elf only chase the target if the target approach the anchor point
     *
     * @param target      entity to chase
     * @param anchor      base entity to anchor to
     * @param anchorSizeX how big the base's area is in the X axis
     * @param anchorSizeY how big the base's area is in the Y axis
     * @return entity
     */

    public static Entity createAnchoredElf(Entity target, Entity anchor, float anchorSizeX, float anchorSizeY) {
        Entity anchoredElf = createBaseNPCNoAI();
        MeleeEnemyConfig config = configs.elfMelee;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new PauseTask())
                        .addTask(new AnchoredWanderTask(
                                anchor, anchorSizeX, anchorSizeY, 2f))
                        .addTask(new AnchoredChaseTask(
                                target, 3f,
                                4f, anchor, anchorSizeX, anchorSizeY))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSizeX, anchorSizeY))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
        animator.addAnimation("moveLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveDown", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("frontDeath", 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation("leftDeath", 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation("rightDeath", 0.5f, Animation.PlayMode.NORMAL);

        anchoredElf
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new ElfAnimationController());

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                HealthBar, HealthBarFrame, HealthBarDecrease);
        anchoredElf.addComponent(healthBarComponent);

        anchoredElf.getComponent(AnimationRenderComponent.class).scaleEntity();

        anchoredElf.setEntityType("melee");

        anchoredElf.setScale(0.6f, 1f);
        PhysicsUtils.setScaledCollider(anchoredElf, 0.9f, 0.2f);
        return anchoredElf;
    }

    /**
     * Creates a ranged elf entity.
     * elf that shoot arrow at target
     * It will retreat if the target is approach in certain range
     *
     * @param target entity to chase
     * @param type   arrow type ("normalArrow", "trackingArrow", "fastArrow")
     * @return entity
     */

    public static Entity createRangedElf(Entity target, String type, float multishotChance) {
        Entity elf = createBaseNPCNoAI();
        RangedEnemyConfig config = configs.elfRanged;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new PauseTask())
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new RangedChaseTask(
                                target, 10, 15f, 20f))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType(type);
        shootProjectileTask.setMultishotChance(multishotChance);
        shootProjectileTask.setShootAnimationTimeMS(500);
        aiComponent.addTask(shootProjectileTask);
        //create fireballs if needed
        elf.data.put("createFireBall", true);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/rangedElf.atlas", TextureAtlas.class));

        if (type.equals("fastArrow")) {
            elf.setEntityType("assassin");
            animator.addAnimation("assassinMoveLeft", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinMoveRight", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinMoveUp", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinMoveDown", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinLeft", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinRight", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinUp", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinDown", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinLeftDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinRightDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinFrontDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinBackDeath", 0.2f, Animation.PlayMode.NORMAL);
        } else {
            elf.setEntityType("ranged");
            animator.addAnimation("rangerMoveLeft", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerMoveRight", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerMoveUp", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerMoveDown", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerLeft", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerRight", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerUp", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rangerDown", 0.1f, Animation.PlayMode.NORMAL);
            animator.addAnimation("leftDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("rightDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("frontDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("backDeath", 0.2f, Animation.PlayMode.NORMAL);

        }

        elf
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new ElfAnimationController())
                .addComponent(aiComponent);

        elf.setAttackRange(5);
        //elf.getComponent(AnimationRenderComponent.class).scaleEntity();
        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                HealthBar, HealthBarFrame, HealthBarDecrease);
        elf.addComponent(healthBarComponent);

        elf.setScale(0.8f, 1f);
        PhysicsUtils.setScaledCollider(elf, 0.9f, 0.2f);
        return elf;
    }

    /**
     * create boss enemy entity
     *
     * @param target enemy to chase (player)
     * @return boss entity
     */
    public static Entity createBossNPC(Entity target) {

        Entity boss = createBaseNPCNoAI();
        ElfBossConfig config = configs.elfBoss;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new PauseTask())
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(
                                target, 10, 7f, 10f))
                        .addTask(new SpawnMinionsTask(target))
                        .addTask(new TeleportationTask(target, 2000))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType("fireBall");
        shootProjectileTask.setMultishotChance(0);
        aiComponent.addTask(shootProjectileTask);
        //Dont create fireballs until ready and on the map
        boss.data.put("createFireBall", false);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/bossAttack.atlas", TextureAtlas.class));
        animator.addAnimation("moveLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("moveDown", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("frontBossDeath", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("backBossDeath", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("leftBossDeath", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("rightBossDeath", 0.1f, Animation.PlayMode.NORMAL);


        boss
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new ElfAnimationController())
                .addComponent(aiComponent)
                .addComponent(new BossOverlayComponent());
        boss.setAttackRange(5);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();
        boss.scaleWidth(2);
        boss.scaleHeight(2);
        boss.getComponent(BossOverlayComponent.class).nameBoss("Elf King");

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        boss.addComponent(healthBarComponent);
        boss.setEntityType("elfBoss");
        boss.setScale(0.8f * 2, 1f * 2);
        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.2f);
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
        return createBaseNPCNoAI()
                .addComponent(aiComponent);
    }

    /**
     * Creates a generic NPC, with no ai,
     * to be used as a base entity by more specific NPC creation methods.
     *
     * @return entity
     */
    private static Entity createBaseNPCNoAI() {
        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.5f));

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.2f);
        return npc;
    }
}
