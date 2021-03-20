package com.deco2800.game.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import com.deco2800.game.ecs.EntityService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import org.junit.jupiter.api.Test;

class ServiceLocatorTest {
  @Test
  void shouldGetSetServices() {
    EntityService entityService = new EntityService();
    RenderService renderService = new RenderService();
    PhysicsService physicsService = mock(PhysicsService.class);
    GameTime gameTime = new GameTime();

    ServiceLocator.registerEntityService(entityService);
    ServiceLocator.registerRenderService(renderService);
    ServiceLocator.registerPhysicsService(physicsService);
    ServiceLocator.registerTimeSource(gameTime);

    assertEquals(ServiceLocator.getEntityService(), entityService);
    assertEquals(ServiceLocator.getRenderService(), renderService);
    assertEquals(ServiceLocator.getPhysicsService(), physicsService);
    assertEquals(ServiceLocator.getTimeSource(), gameTime);

    ServiceLocator.clear();
    assertNull(ServiceLocator.getEntityService());
    assertNull(ServiceLocator.getRenderService());
    assertNull(ServiceLocator.getPhysicsService());
    assertNull(ServiceLocator.getTimeSource());
  }
}