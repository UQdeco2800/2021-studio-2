package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.tasks.AttackTask;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.WaitTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
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
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class ArrowFactory {
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    public static Entity createArrow(Entity target) {
        Entity arrow = createBaseNPC(target);
        BaseEntityConfig config = configs.arrow;



        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
        animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

        arrow
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());

        arrow.getComponent(AnimationRenderComponent.class).scaleEntity();


        return arrow;
    }

    /**
     * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
     *
     * @return entity
     */
    private static Entity createBaseNPC(Entity target) {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AttackTask(target, 20, 10f, 10f));
        Entity npc =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/arrow1.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new PlayerActions())
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1f))
                        .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(npc, 0.5f, 0.4f);
        return npc;
    }

    private void NPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}

