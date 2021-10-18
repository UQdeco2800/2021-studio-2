package com.deco2800.game.components.pause;

import com.badlogic.gdx.Input;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class PauseMenuActionsTest {
    PauseInputComponent input;
    @Mock
    PauseMenuActions actions;
    Entity entity;


    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerInputService(new InputService());
        entity = new Entity();
        actions = mock(PauseMenuActions.class);
        input = new PauseInputComponent();
        entity.addComponent(actions);
        entity.addComponent(input);
        entity.create();
    }

    @AfterEach
    void afterEach() {
        entity.dispose();
        ServiceLocator.clear();
    }

    @Test
    void createTest() {
        verify(actions).create();
    }

    @Test
    void disposeTest() {
        entity.dispose();
        verify(actions).dispose();
    }

    @Test
    void togglePauseTest() {
        //ensure user inputs triggers toggle pause
        entity.getEvents().addListener("togglePause", actions::togglePauseGame);
        input.keyDown(Input.Keys.ESCAPE);
        verify(actions).togglePauseGame();
    }
}
