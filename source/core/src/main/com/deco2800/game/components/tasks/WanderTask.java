package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.math.RandomUtils;
import com.deco2800.game.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);

  private final Vector2 wanderRange;
  private final float waitTime;
  private Vector2 startPos;
  private Entity entity;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;

  public WanderTask(Vector2 wanderRange, float waitTime) {
    this.wanderRange = wanderRange;
    this.waitTime = waitTime;
  }

  @Override
  public int getPriority() {
    return 1;
  }

  @Override
  public void start(Entity entity) {
    this.entity = entity;
    startPos = entity.getPosition().cpy();
    waitTask = new WaitTask(waitTime);
    movementTask = new MovementTask(getRandomPosInRange());
    movementTask.start(entity);
    currentTask = movementTask;
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.Active) {
      if (currentTask == movementTask) {
        startWaiting();
      } else {
        startMoving();
      }
    }
    currentTask.update();
  }

  @Override
  public void stop() {}

  @Override
  public Status getStatus() {
    // Wandering never finishes
    return Status.Active;
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    waitTask.start(entity);
    currentTask = waitTask;
  }

  private void startMoving() {
    logger.debug("Starting moving");
    movementTask.setTarget(getRandomPosInRange());
    movementTask.start(entity);
    currentTask = movementTask;
  }

  private Vector2 getRandomPosInRange() {
    Vector2 halfRange = wanderRange.cpy().scl(0.5f);
    Vector2 min = startPos.cpy().sub(halfRange);
    Vector2 max = startPos.cpy().add(halfRange);
    return RandomUtils.random(min, max);
  }
}
