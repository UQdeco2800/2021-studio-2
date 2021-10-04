package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class EntityHoverTaskTest {
    @Mock
    GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    void triggerWanderEvent() { //Should be the same as wander task but only move within based range
        Entity boss = new Entity();
        EntityHoverTask entityHoverTask = new EntityHoverTask(boss,
                0.1f, 0, new Vector2(0, 1), 1.5f);
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(entityHoverTask);
        boss.addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
        boss.create();

        EventListener0 call = mock(EventListener0.class);
        boss.getEvents().addListener("wanderStart", call);
        entityHoverTask.start();
        verify(call).handle();
    }
}