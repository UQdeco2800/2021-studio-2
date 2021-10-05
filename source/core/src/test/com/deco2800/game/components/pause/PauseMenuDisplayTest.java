package com.deco2800.game.components.pause;

import com.deco2800.game.GdxGame;
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
class PauseMenuDisplayTest {
    PauseMenuActions actions;
    @Mock
    Entity entity;
    PauseMenuDisplay pauseMenu;


    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        entity = new Entity();
        actions = new PauseMenuActions(new GdxGame());
        pauseMenu = mock(PauseMenuDisplay.class);
        entity.addComponent(actions);
        entity.addComponent(pauseMenu);
        entity.create();
    }

    @AfterEach
    void afterEach() {
        entity.dispose();
        ServiceLocator.clear();
    }

    @Test
    public void createTest() {
        verify(pauseMenu).create();
    }

    @Test
    public void disposeTest() {
        entity.dispose();
        verify(pauseMenu).dispose();
    }

    @Test
    public void toggleMenuTest(){
        //ensure that toggling pause toggles pause menu
        entity.getEvents().addListener("toggleMenu", pauseMenu::toggleMenu);
        actions.togglePauseGame();
        verify(pauseMenu).toggleMenu();
    }

}