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
import com.deco2800.game.components.npc.ElfAnimationController;
import com.deco2800.game.components.npc.HumanAnimationController;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.components.tasks.loki.FirePillarTask;
import com.deco2800.game.components.tasks.loki.SpawnDecoysTask;
import com.deco2800.game.components.tasks.loki.SpawnLokiDecoyTask;
import com.deco2800.game.components.tasks.thor.ShootLightningTask;
import com.deco2800.game.components.tasks.thor.ThorAnimationController;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

import java.security.SecureRandom;


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
     * Final String variable to define the right movement for animations.
     */
    private static final String MOVE_RIGHT = "moveRight";

    /**
     * Final String variable to define the left movement for animations.
     */
    private static final String MOVE_LEFT = "moveLeft";

    /**
     * Final String variable to define the up movement for animations.
     */
    private static final String MOVE_UP = "moveUp";

    /**
     * Final String variable to define the down movement for animations.
     */
    private static final String MOVE_DOWN = "moveDown";

    /**
     * Final String variable to define the front death animations.
     */
    private static final String FRONT_DEATH = "frontDeath";

    /**
     * Final String variable to define the left death animations.
     */
    private static final String LEFT_DEATH = "leftDeath";

    /**
     * Final String variable to define the right death animations.
     */
    private static final String RIGHT_DEATH = "rightDeath";

    /**
     * Final String variable to define the back death animations.
     */
    private static final String BACK_DEATH = "backDeath";

    /**
     * Final String variable to define the down attack animations.
     */
    private static final String ATTACK_DOWN = "attackDown";

    /**
     * Final String variable to define the up attack animations.
     */
    private static final String ATTACK_UP = "attackUp";

    /**
     * Final String variable to define the left attack animations.
     */
    private static final String ATTACK_LEFT = "attackLeft";

    /**
     * Final String variable to define the right attack animations.
     */
    private static final String ATTACK_RIGHT = "attackRight";

    /**
     * Final String variable to define the left stun animations.
     */
    private static final String STUN_LEFT = "stunLeft";

    /**
     * Final String variable to define the right stun animations.
     */
    private static final String STUN_RIGHT = "stunRight";

    /**
     * Final String variable to define the down stun animations.
     */
    private static final String STUN_DOWN = "stunDown";
    /**
     * Default animation
     */
    private static final String DEFAULT_ANIMATION = "default";

    /**
     * Final String variable to define the up stun animations.
     */
    private static final String STUN_UP = "stunUp";
    private static final String CREATE_FIREBALL = "createFireBall";
    private static final String VIKING_TYPE = "viking";
    private static final String LOKI_ATLAS = "images/lokiBoss.atlas";

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
        AnimationRenderComponent animator = meleeElfAnimation(0);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ZigChaseTask(
                                target, 11, 4f, 4f, 1))
                        .addTask(new AlertableChaseTask(
                                target, 10, 4f, 4f))
                        .addTask(new PauseTask())
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        elf
                .addComponent(new CombatStatsComponent(MeleeEnemyConfig.HEALTH, MeleeEnemyConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new ElfAnimationController());

        elf.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        elf.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));

        elf.addComponent(createHealthBarComponent());

        elf.getComponent(AnimationRenderComponent.class).scaleEntity();
        elf.setScale(1f, 1.3f);
        elf.setEntityType("melee");
        PhysicsUtils.setScaledCollider(elf, 0.9f, 0.2f);

        elf.setAttackRange(6);
        return elf;
    }

    public static Entity createElfGuard(Entity target) {
        Entity elfGuard = createBaseNPCNoAI();
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                .addTask(new AlertChaseTask(target, 15, 3f, 4f))
                .addTask(new PauseTask())
                .addTask(new DeathPauseTask(
                        target, 0, 100, 100, 1.5f));
        elfGuard.addComponent(aiTaskComponent);

        AnimationRenderComponent animator = meleeElfAnimation(1);

        elfGuard
                .addComponent(new CombatStatsComponent(MeleeEnemyConfig.HEALTH, MeleeEnemyConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new ElfAnimationController());

        elfGuard.setEntityType("AlertCaller");

        elfGuard.addComponent(createHealthBarComponent());

        elfGuard.getComponent(AnimationRenderComponent.class).scaleEntity();
        elfGuard.setScale(0.6f, 1f);
        elfGuard.setScale(1f, 1.3f);
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
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AnchoredWanderTask(anchor, anchorSize, 2f))
                        .addTask(new AnchoredChaseTask(
                                target, 3f, 4f, anchor, anchorSize))
                        .addTask(new PauseTask())
                        .addTask(new AnchoredRetreatTask(anchor, anchorSize))
                        .addTask(new AlertableChaseTask(
                                target, 10, 3f, 4f))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        anchoredElf.addComponent(aiComponent);

        AnimationRenderComponent animator = meleeElfAnimation(0);

        anchoredElf
                .addComponent(new CombatStatsComponent(MeleeEnemyConfig.HEALTH, MeleeEnemyConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new ElfAnimationController());

        anchoredElf.addComponent(createHealthBarComponent());

        anchoredElf.getComponent(AnimationRenderComponent.class).scaleEntity();
        anchoredElf.setScale(1f, 1.3f);
        anchoredElf.setEntityType("melee");
        PhysicsUtils.setScaledCollider(anchoredElf, 0.9f, 0.2f);
        anchoredElf.setAttackRange(6);
        return anchoredElf;
    }

    private static AnimationRenderComponent meleeElfAnimation(int type) {
        AnimationRenderComponent animator;
        if (type == 1) {
            animator = new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/meleeFinal.atlas", TextureAtlas.class));
        } else {
            animator = new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/guardFinal.atlas", TextureAtlas.class));
        }
        animator.addAnimation(MOVE_LEFT, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_RIGHT, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_UP, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_DOWN, 0.4f, Animation.PlayMode.LOOP);

        animator.addAnimation(FRONT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(LEFT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(RIGHT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(BACK_DEATH, 0.2f, Animation.PlayMode.NORMAL);

        animator.addAnimation(STUN_LEFT, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(STUN_RIGHT, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(STUN_UP, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(STUN_DOWN, 0.5f, Animation.PlayMode.NORMAL);

        animator.addAnimation(ATTACK_DOWN, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(ATTACK_LEFT, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(ATTACK_RIGHT, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(ATTACK_UP, 0.4f, Animation.PlayMode.LOOP);
        return animator;
    }

    /**
     * Creates a ranged elf entity.
     * elf that shoot arrow at target
     * It will retreat if the target is approach in certain range
     *
     * @param target entity to chase
     * @param type   arrow type
     * @return entity
     */

    public static Entity createRangedElf(Entity target, ShootProjectileTask.projectileTypes type, float multishotChance) {
        Entity elf = createBaseNPCNoAI();
        SecureRandom rand = new SecureRandom();
        double chance = rand.nextDouble();
        if (chance <= multishotChance && type == ShootProjectileTask.projectileTypes.NORMAL_ARROW) {
            type = ShootProjectileTask.projectileTypes.TRACKING_ARROW;
            multishotChance = multishotChance / 2;
        }
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new RangedChaseTask(
                                target, 10, 15f, 20f))
                        .addTask(new PauseTask())
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType(type);
        shootProjectileTask.setMultishotChance(multishotChance);
        shootProjectileTask.setShootAnimationTimeMS(500);
        aiComponent.addTask(shootProjectileTask);
        //create fireballs if needed
        elf.data.put(CREATE_FIREBALL, true);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/rangedAllFinal.atlas", TextureAtlas.class));

        if (type.equals(ShootProjectileTask.projectileTypes.FAST_ARROW)) {
            elf.setEntityType("assassin");
            animator.addAnimation("assassinLeft", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinRight", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinUp", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinDown", 0.5f, Animation.PlayMode.LOOP);

            animator.addAnimation("assassinStunUp", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinStunDown", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinStunLeft", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinStunRight", 0.5f, Animation.PlayMode.LOOP);

            animator.addAnimation("assassinLeftDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinRightDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinFrontDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinBackDeath", 0.2f, Animation.PlayMode.NORMAL);


        } else {
            elf.setEntityType("ranged");
            animator.addAnimation("rangedLeft", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangedRight", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangedUp", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangedDown", 0.5f, Animation.PlayMode.LOOP);

            animator.addAnimation("rangedStunRight", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangedStunLeft", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangedStunDown", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangedStunUp", 0.5f, Animation.PlayMode.LOOP);

            animator.addAnimation(LEFT_DEATH, 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation(RIGHT_DEATH, 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation(FRONT_DEATH, 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation(BACK_DEATH, 0.2f, Animation.PlayMode.NORMAL);


        }

        elf
                .addComponent(new CombatStatsComponent(RangedEnemyConfig.HEALTH, RangedEnemyConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new ElfAnimationController())
                .addComponent(aiComponent);

        elf.setAttackRange(5);
        elf.addComponent(createHealthBarComponent());

        elf.setScale(1.2f, 1.2f);
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
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new AlertableChaseTask(
                                target, 10, 7f, 10f))
                        .addTask(new SpawnMinionsAndExplosionTask(target))
                        .addTask(new TeleportationTask(target, 2000))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType(ShootProjectileTask.projectileTypes.FIREBALL);
        shootProjectileTask.setMultishotChance(0);
        aiComponent.addTask(shootProjectileTask);
        boss.data.put(CREATE_FIREBALL, true);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/bossFinal.atlas", TextureAtlas.class));
        animator.addAnimation(MOVE_LEFT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_RIGHT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_UP, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_DOWN, 0.5f, Animation.PlayMode.LOOP);

        animator.addAnimation(ATTACK_LEFT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(ATTACK_RIGHT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(ATTACK_UP, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(ATTACK_DOWN, 0.5f, Animation.PlayMode.LOOP);

        animator.addAnimation(STUN_LEFT, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(STUN_RIGHT, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(STUN_UP, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(STUN_DOWN, 0.5f, Animation.PlayMode.NORMAL);

        animator.addAnimation("frontBossDeath", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("backBossDeath", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("leftBossDeath", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("rightBossDeath", 0.1f, Animation.PlayMode.NORMAL);

        boss
                .addComponent(new CombatStatsComponent(ElfBossConfig.HEALTH, ElfBossConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new ElfAnimationController())
                .addComponent(aiComponent)
                .addComponent(new BossOverlayComponent());
        boss.setAttackRange(5);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();
        boss.scaleWidth(2);
        boss.scaleHeight(2);
        boss.getComponent(BossOverlayComponent.class).nameBoss("Elf King");

        boss.setEntityType("elfBoss");
        boss.setScale(0.8f * 2, 1f * 2);
        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.2f);

        boss.setAttackRange(10);

        return boss;
    }

    public static Entity createThor(Entity target) {
        Entity thor = createBaseNPCNoAI();
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 5f, 20f))
                        .addTask(new DeathPauseTask(target, 0, 100, 100, 1.5f))
                        .addTask(new ShootLightningTask(target, 2000, 150))
                        .addTask(new PauseTask());

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("thor/thor.atlas",
                                TextureAtlas.class));

        animator.addAnimation("hammer_aoe", 0.1f);
        animator.addAnimation("thor_left_attack", 0.1f);
        animator.addAnimation("thor_right_attack", 0.1f);
        animator.addAnimation("thor_up_attack", 0.1f);
        animator.addAnimation("thor_down_attack", 0.1f);

        animator.addAnimation("up_thor_walk", 0.18f, Animation.PlayMode.LOOP);
        animator.addAnimation("down_thor_walk", 0.13f, Animation.PlayMode.LOOP);
        animator.addAnimation("thor_left_walking", 0.13f, Animation.PlayMode.LOOP);
        animator.addAnimation("thor_right_walking", 0.18f, Animation.PlayMode.LOOP);
        animator.addAnimation("default", 1f, Animation.PlayMode.NORMAL);

        animator.addAnimation(DEFAULT_ANIMATION, 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default_backward", 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default_right", 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default_left", 1f, Animation.PlayMode.NORMAL);

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);
        thor.addComponent(new CombatStatsComponent(ElfBossConfig.HEALTH, ElfBossConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new ThorAnimationController())
                .addComponent(aiComponent)
                .addComponent(healthBarComponent)
                .addComponent(new BossOverlayComponent());
        thor.getComponent(BossOverlayComponent.class).nameBoss("Thor");
        thor.setEntityType("thor");
        thor.scaleWidth(2);
        thor.scaleHeight(2);
        thor.setAttackRange(10);
        return thor;
    }

    public static Entity createOdin(Entity target) {
        Entity odin = createBaseNPCNoAI();

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 20f, 40f))
                        .addTask(new DeathPauseTask(target, 0, 100, 100, 1.5f))
                        .addTask(new PauseTask());
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 3000);
        shootProjectileTask.setProjectileType(ShootProjectileTask.projectileTypes.BEAM);
        shootProjectileTask.setMultishotChance(0);
        shootProjectileTask.setShootAnimationTimeMS(1);
        aiComponent.addTask(shootProjectileTask);
        odin.data.put(CREATE_FIREBALL, true);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("Odin/odin.atlas",
                                TextureAtlas.class));
        animator.startAnimation(DEFAULT_ANIMATION);
        setHumanAnimations(animator);

        odin.addComponent(new CombatStatsComponent(OdinBossConfig.HEALTH, OdinBossConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())
                .addComponent(aiComponent)
                .addComponent(new BossOverlayComponent());
        odin.getComponent(BossOverlayComponent.class).nameBoss("Odin");
        odin.setAttackRange(10);

        odin.getComponent(AnimationRenderComponent.class).scaleEntity();
        odin.setEntityType("odin");
        odin.setScale(0.8f * 2, 1f * 2);

        PhysicsUtils.setScaledCollider(odin, 0.6f, 0.3f);
        return odin;
    }

    /**
     * Creates a melee viking which will be used for the outdoor map.
     *
     * @param target the entity that the NPC will chase and attack
     * @return the enemy entity
     */
    public static Entity createMeleeViking(Entity target) {
        Entity viking = createBaseNPCNoAI();

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/viking.atlas", TextureAtlas.class));

        setHumanAnimations(animator);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ZigChaseTask(
                                target, 11, 4f, 4f, 1))
                        .addTask(new PauseTask())
                        .addTask(new AlertableChaseTask(
                                target, 10, 3f, 4f))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        viking.addComponent(new CombatStatsComponent(MeleeVikingConfig.HEALTH, MeleeVikingConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new HumanAnimationController());
        viking.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        viking.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));

        viking.addComponent(createHealthBarComponent());

        viking.getComponent(AnimationRenderComponent.class).scaleEntity();
        viking.getComponent(AnimationRenderComponent.class).setAnimationScale(2f);
        viking.setEntityType(VIKING_TYPE);
        PhysicsUtils.setScaledCollider(viking, 0.6f, 0.3f);
        return viking;
    }

    /**
     * Creates a melee viking which will be used for the Hell map.
     *
     * @param target the entity that the NPC will chase and attack
     * @return the enemy entity
     */
    public static Entity createMeleeHellViking(Entity target) {
        Entity viking = createBaseNPCNoAI();

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/hellViking.atlas", TextureAtlas.class));

        setHumanAnimations(animator);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(
                                target, 11, 4f, 4f, new Vector2(1.6f, 1.6f)))
                        .addTask(new PauseTask())
                        .addTask(new AlertableChaseTask(
                                target, 10, 3f, 4f))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        viking.addComponent(new CombatStatsComponent(MeleeHellWarriorConfig.HEALTH, MeleeHellWarriorConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new HumanAnimationController());

        viking.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        viking.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));
        viking.addComponent(createHealthBarComponent());

        viking.getComponent(AnimationRenderComponent.class).scaleEntity();
        viking.getComponent(AnimationRenderComponent.class).setAnimationScale(2f);
        viking.setEntityType(VIKING_TYPE);
        PhysicsUtils.setScaledCollider(viking, 0.6f, 0.3f);
        return viking;
    }

    /**
     * Creates a melee viking which will be used for the Asgard map.
     *
     * @param target the entity that the NPC will chase and attack
     * @return the enemy entity
     */
    public static Entity createMeleeAsgardViking(Entity target) {
        Entity viking = createBaseNPCNoAI();

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/asgardWarrior.atlas", TextureAtlas.class));

        setHumanAnimations(animator);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(
                                target, 11, 4f, 4f, new Vector2(1.5f, 1.5f)))
                        .addTask(new PauseTask())
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        viking.addComponent(new CombatStatsComponent(MeleeAsgardWarriorConfig.HEALTH, MeleeAsgardWarriorConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new HumanAnimationController());
        viking.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        viking.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));
        viking.addComponent(createHealthBarComponent());

        viking.getComponent(AnimationRenderComponent.class).scaleEntity();
        viking.getComponent(AnimationRenderComponent.class).setAnimationScale(2f);
        viking.setEntityType(VIKING_TYPE);
        PhysicsUtils.setScaledCollider(viking, 0.6f, 0.3f);
        return viking;
    }

    /**
     * Sets the required animations for a melee enemy
     *
     * @param animator the animation component of the entity
     */
    private static void setHumanAnimations(AnimationRenderComponent animator) {

        animator.addAnimation(DEFAULT_ANIMATION, 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("defaultLeft", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("defaultRight", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("defaultUp", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation(MOVE_LEFT, 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_RIGHT, 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_UP, 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_DOWN, 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation(FRONT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(BACK_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(LEFT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(RIGHT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackDown", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackUp", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackLeft", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackRight", 0.05f, Animation.PlayMode.NORMAL);
    }

    /**
     * Creates a ranged elf entity.
     * elf that shoot arrow at target
     * It will retreat if the target is approach in certain range
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createOutdoorArcher(Entity target) {
        Entity archer = createBaseNPCNoAI();
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new RangedChaseTask(
                                target, 10, 15f, 20f))
                        .addTask(new PauseTask())
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType(ShootProjectileTask.projectileTypes.FAST_ARROW);
        shootProjectileTask.setShootAnimationTimeMS(200);
        aiComponent.addTask(shootProjectileTask);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/outdoorArcher.atlas", TextureAtlas.class));

        animator.setAnimationScale(2f);

        setHumanAnimations(animator);

        archer
                .addComponent(new CombatStatsComponent(RangedEnemyConfig.HEALTH, RangedEnemyConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())
                .addComponent(aiComponent);

        archer.setAttackRange(5);
        archer.addComponent(createHealthBarComponent());
        archer.setEntityType("archer");

        PhysicsUtils.setScaledCollider(archer, 0.4f, 0.3f);

        return archer;
    }

    /**
     * create Loki boss enemy entity
     *
     * @param target enemy to chase (player)
     * @return boss entity
     */
    public static Entity createLoki(Entity target) {

        Entity boss = createBaseNPCNoAI();
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new PauseTask())
                        .addTask(new FirePillarTask(target, 800, 150))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f))
                        .addTask(new RangedChaseTask(
                                target, 10, 20f, 20f))
                        .addTask(new SpawnDecoysTask(target))
                        .addTask(new SpawnLokiDecoyTask(target, 8000));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(LOKI_ATLAS, TextureAtlas.class));

        animator.addAnimation("transformedMoveLeft", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("transformedMoveRight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("transformedMoveDown", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("transformedMoveUp", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("defaultTransformedLeft", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("defaultTransformedRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("defaultTransformedUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("defaultTransformed", 0.1f, Animation.PlayMode.NORMAL);

        animator.setAnimationScale(2f);

        setHumanAnimations(animator);

        boss
                .addComponent(new CombatStatsComponent(LokiBossConfig.HEALTH, LokiBossConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())

                .addComponent(aiComponent)
                .addComponent(new BossOverlayComponent());
        boss.setAttackRange(3);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();
        boss.getComponent(BossOverlayComponent.class).nameBoss("Loki");

        boss.setEntityType("loki");
        PhysicsUtils.setScaledCollider(boss, 0.6f, 0.3f);
        return boss;
    }

    /**
     * create Loki boss enemy entity
     *
     * @param target enemy to chase (player)
     * @return boss entity
     */
    public static Entity createRangedLokiDecoy(Entity target) {
        Entity boss = createBaseNPCNoAI();
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new PauseTask())
                        .addTask(new FirePillarTask(target, 1000, 150))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f))
                        .addTask(new RangedChaseTask(
                                target, 10, 20f, 20f))
                        .addTask(new SpawnLokiDecoyTask(target, 10000));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(LOKI_ATLAS, TextureAtlas.class));
        animator.setAnimationScale(2f);

        setHumanAnimations(animator);

        boss
                .addComponent(new CombatStatsComponent(1, LokiBossConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())
                .addComponent(aiComponent);
        boss.setAttackRange(3);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();

        boss.addComponent(createHealthBarComponent());
        boss.setEntityType("decoy");
        boss.getComponent(ColliderComponent.class).setDensity(1.5f);
        PhysicsUtils.setScaledCollider(boss, 0.6f, 0.3f);
        return boss;
    }

    /**
     * create Loki boss enemy entity
     *
     * @param target enemy to chase (player)
     * @return boss entity
     */
    public static Entity createMeleeLokiDecoy(Entity target) {
        Entity viking = createBaseNPCNoAI();

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(LOKI_ATLAS, TextureAtlas.class));

        setHumanAnimations(animator);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(
                                target, 11, 4f, 4f, new Vector2(2f, 2f)))
                        .addTask(new PauseTask())
                        .addTask(new AlertableChaseTask(
                                target, 10, 3f, 4f))
                        .addTask(new DeathPauseTask(
                                target, 0, 100, 100, 1.5f));

        viking
                .addComponent(new CombatStatsComponent(1, MeleeHellWarriorConfig.BASE_ATTACK))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new HumanAnimationController());

        viking.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        viking.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));

        viking.addComponent(createHealthBarComponent());

        viking.getComponent(AnimationRenderComponent.class).scaleEntity();
        viking.getComponent(AnimationRenderComponent.class).setAnimationScale(2f);
        viking.setEntityType(VIKING_TYPE);
        PhysicsUtils.setScaledCollider(viking, 0.6f, 0.3f);
        return viking;
    }

    /**
     * Creates a health bar component and returns it for the enemy NPC
     *
     * @return HealthBarComponent which will be displayed above the entities
     */
    private static HealthBarComponent createHealthBarComponent() {
        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        return new HealthBarComponent(healthBar, healthBarFrame, healthBarDecrease);
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
