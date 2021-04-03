package com.deco2800.game.ai.tasks;

import com.deco2800.game.entities.Entity;

public interface Task {
  void start(Entity owner);
  void update();
  void stop();
  Status getStatus();

  enum Status {
    Finished, Failed, Active, Inactive
  }
}
