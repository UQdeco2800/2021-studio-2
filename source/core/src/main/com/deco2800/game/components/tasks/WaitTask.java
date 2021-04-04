package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class WaitTask implements Task {
  private final GameTime timeSource;
  private final float duration;
  private long endTime;

  public WaitTask(float duration) {
    timeSource = ServiceLocator.getTimeSource();
    this.duration = duration;
  }

  @Override
  public void start(Entity owner) {
    endTime = timeSource.getTime() + (int)(duration * 1000);
  }

  @Override
  public void update() {

  }

  @Override
  public void stop() {

  }

  @Override
  public Status getStatus() {
    if (timeSource.getTime() >= endTime) {
      return Status.Finished;
    }
    return Status.Active;
  }
}
