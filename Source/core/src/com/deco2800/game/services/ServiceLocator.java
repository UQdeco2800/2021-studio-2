package com.deco2800.game.services;

import com.deco2800.game.ecs.EntityService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services. Warning: global access is a trap and should
 * be used extremely sparingly. Read the README for details.
 */
public class ServiceLocator {
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static void registerEntityService(EntityService service) {
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    physicsService = service;
  }
}
