package com.deco2800.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class CutsceneScreenTest {

    @Mock
    CutsceneScreen cutsceneScreen;

    @Mock
    SpriteBatch spriteBatch;

    @BeforeEach
    void setUp() {
        ServiceLocator.registerTimeSource(new GameTime());
        Entity ui = new Entity();
        ui.addComponent(mock(CutsceneScreen.class));
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.getEntityService().registerUI(ui);

        cutsceneScreen = ServiceLocator.getEntityService().getUIEntity().getComponent(CutsceneScreen.class);
    }

    @Test
    void shouldSetOpen() {
        CutsceneScreen cutsceneScreen = new CutsceneScreen();

        cutsceneScreen.setOpen();
        assertTrue(cutsceneScreen.isOpen());
    }

    @Test
    void shouldSetClosed() {
        CutsceneScreen cutsceneScreen = new CutsceneScreen();
        cutsceneScreen.setClosed();

        cutsceneScreen.setClosed();
        assertFalse(cutsceneScreen.isOpen());
    }

    @Test
    void shouldSetClosedMock() {
        cutsceneScreen.setClosed();
        cutsceneScreen.draw(spriteBatch);

        verify(cutsceneScreen).setClosed();
    }
}