package com.deco2800.game.components.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.utils.math.Vector2Utils;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class AnchoredWanderTaskTest {
    @Mock
    GameTime gameTime;

    @BeforeEach
    void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    void triggerWanderEvent() { //Should be the same as wander task but only move within based range
        Entity anchor = new Entity();
        AnchoredWanderTask anchoredWanderTask = new AnchoredWanderTask(anchor, 2f, 3f, 2f);
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(anchoredWanderTask);
        Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
        entity.create();

        EventListener0 call = mock(EventListener0.class);
        entity.getEvents().addListener("wanderStart", call);
        anchoredWanderTask.start();
        verify(call).handle();
    }
}