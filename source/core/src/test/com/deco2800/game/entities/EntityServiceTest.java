package com.deco2800.game.entities;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class EntityServiceTest {
    @Test
    void shouldCreateEntity() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        verify(entity).create();
    }

    @Test
    void shouldUpdateEntities() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        entityService.update();

        verify(entity).update();
        verify(entity).earlyUpdate();
    }

    @Test
    void shouldNotUpdateUnregisteredEntities() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        entityService.unregister(entity);
        entityService.update();
        verify(entity, times(0)).update();
        verify(entity, times(0)).earlyUpdate();
    }

    @Test
    void shouldDisposeEntities() {
        EntityService entityService = new EntityService();
        Entity entity = mock(Entity.class);
        entityService.register(entity);
        entityService.dispose();
        verify(entity).dispose();
    }
} 
