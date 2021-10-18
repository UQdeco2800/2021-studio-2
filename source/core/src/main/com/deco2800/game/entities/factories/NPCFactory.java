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
import com.deco2800.game.components.touch.TouchAttackComponent;
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
     * Final String variable to define the up stun animations.
     */
    private static final String STUN_UP = "stunUp";

    /**
     * load attribute from config
     */
    private static final NPCConfigs configs = new NPCConfigs();

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
//                new AnimationRenderComponent(
//                        ServiceLocator.getResourceService().getAsset("images/meleeFinal.atlas", TextureAtlas.class));
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));

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
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new ElfAnimationController());

        elf.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        elf.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f, 1));

        elf.addComponent(createHealthBarComponent());

        elf.getComponent(AnimationRenderComponent.class).scaleEntity();
        //elf.setScale(0.6f, 1f);
        elf.setScale(1f, 1.3f);
        elf.setEntityType("melee");
        PhysicsUtils.setScaledCollider(elf, 0.9f, 0.2f);

        elf.setAttackRange(6);
        return elf;
    }

    public static Entity createElfGuard(Entity target) {
        Entity elfGuard = createBaseNPCNoAI();
        MeleeEnemyConfig config = configs.elfMelee;
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                .addTask(new AlertChaseTask(target, 15, 3f, 4f))
                .addTask(new PauseTask())
                .addTask(new DeathPauseTask(
                        target, 0, 100, 100, 1.5f));
        elfGuard.addComponent(aiTaskComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/guardElf.atlas", TextureAtlas.class));
//                new AnimationRenderComponent(
//                        ServiceLocator.getResourceService().getAsset("images/guardFinal.atlas", TextureAtlas.class));
        animator.addAnimation(MOVE_LEFT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_RIGHT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_UP, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_DOWN, 0.5f, Animation.PlayMode.LOOP);

//        animator.addAnimation(STUN_LEFT, 0.5f, Animation.PlayMode.NORMAL);
//        animator.addAnimation(STUN_RIGHT, 0.5f, Animation.PlayMode.NORMAL);
//        animator.addAnimation(STUN_UP, 0.5f, Animation.PlayMode.NORMAL);
//        animator.addAnimation(STUN_DOWN, 0.5f, Animation.PlayMode.NORMAL);

        animator.addAnimation(FRONT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(LEFT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(RIGHT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(BACK_DEATH, 0.2f, Animation.PlayMode.NORMAL);
//
//        animator.addAnimation(ATTACK_DOWN, 0.4f, Animation.PlayMode.LOOP);
//        animator.addAnimation(ATTACK_LEFT, 0.4f, Animation.PlayMode.LOOP);
//        animator.addAnimation(ATTACK_RIGHT, 0.4f, Animation.PlayMode.LOOP);
//        animator.addAnimation(ATTACK_UP, 0.4f, Animation.PlayMode.LOOP);

        elfGuard
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
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
        MeleeEnemyConfig config = configs.elfMelee;
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

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
//                new AnimationRenderComponent(
//                        ServiceLocator.getResourceService().getAsset("images/meleeFinal.atlas", TextureAtlas.class));
        animator.addAnimation(MOVE_LEFT, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_RIGHT, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_UP, 0.4f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_DOWN, 0.4f, Animation.PlayMode.LOOP);

        animator.addAnimation(FRONT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(LEFT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(RIGHT_DEATH, 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation(BACK_DEATH, 0.2f, Animation.PlayMode.NORMAL);

//        animator.addAnimation(STUN_LEFT, 0.5f, Animation.PlayMode.NORMAL);
//        animator.addAnimation(STUN_RIGHT, 0.5f, Animation.PlayMode.NORMAL);
//        animator.addAnimation(STUN_UP, 0.5f, Animation.PlayMode.NORMAL);
//        animator.addAnimation(STUN_DOWN, 0.5f, Animation.PlayMode.NORMAL);
//
//        animator.addAnimation(ATTACK_DOWN, 0.4f, Animation.PlayMode.LOOP);
//        animator.addAnimation(ATTACK_LEFT, 0.4f, Animation.PlayMode.LOOP);
//        animator.addAnimation(ATTACK_RIGHT, 0.4f, Animation.PlayMode.LOOP);
//        animator.addAnimation(ATTACK_UP, 0.4f, Animation.PlayMode.LOOP);


        anchoredElf
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
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
        RangedEnemyConfig config = configs.elfRanged;
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
        elf.data.put("createFireBall", true);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/rangedElf.atlas", TextureAtlas.class));
//                new AnimationRenderComponent(
//                        ServiceLocator.getResourceService().getAsset("images/rangedAllFinal.atlas", TextureAtlas.class));


        if (type.equals(ShootProjectileTask.projectileTypes.FAST_ARROW)) {
            elf.setEntityType("assassin");
            animator.addAnimation("assassinLeft", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinRight", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinUp", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("assassinDown", 0.5f, Animation.PlayMode.LOOP);

//            animator.addAnimation("assassinStunLeft", 0.5f, Animation.PlayMode.NORMAL);
//            animator.addAnimation("assassinStunRight", 0.5f, Animation.PlayMode.NORMAL);
//            animator.addAnimation("assassinStunUp", 0.5f, Animation.PlayMode.NORMAL);
//            animator.addAnimation("assassinStunDown", 0.5f, Animation.PlayMode.NORMAL);

            animator.addAnimation("assassinLeftDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinRightDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinFrontDeath", 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation("assassinBackDeath", 0.2f, Animation.PlayMode.NORMAL);


        } else {
            elf.setEntityType("ranged");
            animator.addAnimation("rangerLeft", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangerRight", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangerUp", 0.5f, Animation.PlayMode.LOOP);
            animator.addAnimation("rangerDown", 0.5f, Animation.PlayMode.LOOP);
//
//            animator.addAnimation("rangerStunLeft", 0.5f, Animation.PlayMode.NORMAL);
//            animator.addAnimation("rangerStunRight", 0.5f, Animation.PlayMode.NORMAL);
//            animator.addAnimation("rangerStunUp", 0.5f, Animation.PlayMode.NORMAL);
//            animator.addAnimation("rangerStunDown", 0.5f, Animation.PlayMode.NORMAL);

            animator.addAnimation(LEFT_DEATH, 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation(RIGHT_DEATH, 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation(FRONT_DEATH, 0.2f, Animation.PlayMode.NORMAL);
            animator.addAnimation(BACK_DEATH, 0.2f, Animation.PlayMode.NORMAL);


        }

        elf
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
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
        ElfBossConfig config = configs.elfBoss;
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
        //Dont create fireballs until ready and on the map
        boss.data.put("createFireBall", false);

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

        boss.addComponent(createHealthBarComponent());
        boss.setEntityType("elfBoss");
        boss.setScale(0.8f * 2, 1f * 2);
        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.2f);

        boss.setAttackRange(10);

        return boss;
    }

    public static Entity createThor(Entity target) {
        Entity thor = createBaseNPCNoAI();
        ElfBossConfig config = configs.elfBoss;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 5f, 20f))
                        .addTask(new DeathPauseTask(target, 0, 100, 100, 1.5f));
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("thor/thor.atlas",
                                TextureAtlas.class));
        animator.addAnimation(MOVE_LEFT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_RIGHT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_UP, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(MOVE_DOWN, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("EnemyAttackDown", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackUp", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackLeft", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("EnemyAttackRight", 0.05f, Animation.PlayMode.NORMAL);

        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(
                healthBar, healthBarFrame, healthBarDecrease);

        thor.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())
                .addComponent(aiComponent)
                .addComponent(healthBarComponent);
        thor.scaleWidth(2);
        thor.scaleHeight(2);
        return thor;
    }

    public static Entity createOdin(Entity target) {
        Entity odin = createBaseNPCNoAI();
        ElfBossConfig config = configs.elfBoss;

//--------THE AI COMPONENT WHERE IT DOES THE ATTACK AND SUMMONING AND WHEN IT DIES SPAWN PORTAL-----
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 5f, 20f))
                        .addTask(new DeathPauseTask(target, 0, 100, 100, 1.5f))
                        .addTask(new PauseTask());

        // --------------------------------THE ANIMATION FOR THIS BOSS------------------------------------
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("Odin/odin.atlas",
                                TextureAtlas.class));

        animator.setAnimationScale(1f); //maybe?
        animator.startAnimation("default");
        setHumanAnimations(animator);

        //--- ---------------------ADD OTHER COMPONENTS OTHER THAN AI, ANIMATIONS---------------------
        odin.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())
                .addComponent(aiComponent);
//            .addComponent(new BossOverlayComponent());
//        odin.getComponent(BossOverlayComponent.class).nameBoss("God King: ODIN");

        // ------------------------ITS ATTACK RANGE AND SCALLING?-----------------------------------------
        odin.setAttackRange(5);
        //odin.getComponent(AnimationRenderComponent.class).scaleEntity();
        odin.scaleWidth(2);  //MORE SCALLING HERE AND AT THE BOTTOM???
        odin.scaleHeight(2);
//
//
//-------------------ITS MASSIVE HEALTH BAR ON TOP OF ITS HEAD-------------------------------------
        odin.addComponent(createHealthBarComponent());

// ----------------------------FINAL SETTINGS FOR IN-GAME LOOKS-------------------------------------
        odin.setEntityType("odin"); //MAYBE NO USE? use for AI tasks
        odin.setScale(0.8f * 2, 1f * 2); //Entity SCALING
        PhysicsUtils.setScaledCollider(odin, 0.9f, 0.2f); //COLLIDER HIT BOX SCALER?
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
        MeleeVikingConfig config = configs.vikingMelee;

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
        viking.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
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
        viking.setEntityType("viking");
        PhysicsUtils.setScaledCollider(viking, 0.9f, 0.6f);
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
        MeleeHellWarriorConfig config = configs.hellWarriorMelee;

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

        viking.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
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
        viking.setEntityType("viking");
        PhysicsUtils.setScaledCollider(viking, 0.9f, 0.6f);
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
        MeleeAsgardWarriorConfig config = configs.asgardWarriorMelee;

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

        viking.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
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
        viking.setEntityType("viking");
        PhysicsUtils.setScaledCollider(viking, 0.9f, 0.6f);
        return viking;
    }

    /**
     * Sets the required animations for a melee enemy
     *
     * @param animator the animation component of the entity
     */
    private static void setHumanAnimations(AnimationRenderComponent animator) {

        animator.addAnimation("default", 0.2f, Animation.PlayMode.NORMAL);
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
        RangedEnemyConfig config = configs.elfRanged;
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
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())
                .addComponent(aiComponent);

        archer.setAttackRange(5);
        archer.addComponent(createHealthBarComponent());
        archer.setEntityType("archer");

        PhysicsUtils.setScaledCollider(archer, 0.6f, 0.2f);
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
        LokiBossConfig config = configs.lokiBoss;
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
                        ServiceLocator.getResourceService().getAsset("images/lokiBoss.atlas", TextureAtlas.class));

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
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new HumanAnimationController())

                .addComponent(aiComponent)
                .addComponent(new BossOverlayComponent());
        boss.setAttackRange(3);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();
        boss.getComponent(BossOverlayComponent.class).nameBoss("Loki    ");

        boss.addComponent(createHealthBarComponent());
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
        LokiBossConfig config = configs.lokiBoss;
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
                        ServiceLocator.getResourceService().getAsset("images/lokiBoss.atlas", TextureAtlas.class));
        animator.setAnimationScale(2f);

        setHumanAnimations(animator);

        boss
                .addComponent(new CombatStatsComponent(1, config.baseAttack))
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
        MeleeHellWarriorConfig config = configs.hellWarriorMelee;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/lokiBoss.atlas", TextureAtlas.class));

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
                .addComponent(new CombatStatsComponent(1, config.baseAttack))
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
        viking.setEntityType("viking");
        PhysicsUtils.setScaledCollider(viking, 0.9f, 0.6f);
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
