package com.deco2800.game.services;

import com.deco2800.game.ecs.EntityService;

public class ServiceLocator {
  private static EntityService entityService;

  public static EntityService getEntityService() {
    return entityService;
  }

  public static void registerEntityService(EntityService service) {
    entityService = service;
  }
}
