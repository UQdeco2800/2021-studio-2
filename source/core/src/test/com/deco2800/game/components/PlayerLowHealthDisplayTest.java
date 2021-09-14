package com.deco2800.game.components;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.deco2800.game.components.player.PlayerLowHealthDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class PlayerLowHealthDisplayTest {
    @Mock PlayerLowHealthDisplay lowHealth;
    Entity entity;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerEntityService(new EntityService());
        entity = new Entity();
        lowHealth = mock(PlayerLowHealthDisplay.class);
        entity.addComponent(lowHealth);
        entity.create();
    }

    @AfterEach
    void afterEach() {
        entity.dispose();
        ServiceLocator.clear();
    }

    @Test
    void createComponent() {
        verify(lowHealth).create();
        entity.dispose();
        verify(lowHealth).dispose();
    }
}
