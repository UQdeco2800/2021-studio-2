package com.deco2800.game.areas;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class GameAreaTest {
    @Test
    void shouldSpawnEntities() {
        GameArea gameArea =
                new GameArea() {
                    @Override
                    public void create(String mapFile, String areaName) {
                    }
                };

        ServiceLocator.registerEntityService(new EntityService());
        Entity entity = mock(Entity.class);

        gameArea.spawnEntity(entity);
        verify(entity).create();

        gameArea.dispose();
        verify(entity).dispose();
    }
}
