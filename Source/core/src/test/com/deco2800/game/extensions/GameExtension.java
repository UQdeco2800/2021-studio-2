package com.deco2800.game.extensions;

import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A JUnit extension which provides libGDX mocking and clears global variables between tests. Use
 * this extension when testing game-related classes.
 */
public class GameExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) {
    // Clear the global state from the service locator
    ServiceLocator.clear();
  }
}
