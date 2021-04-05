package com.deco2800.game.ai.tasks;

import com.deco2800.game.entities.Entity;

/**
 * An AI task can be started and stopped at any time. When updating, the task can succeed or fail.
 */
public interface Task {
  /**
   * Start running this task. This will usually be called by an AI controller.
   *
   * @param entity Entity on which this task is starting.
   */
  void start(Entity entity);

  /** Run one frame of the task. Similar to the update() in Components. */
  void update();

  /** Stop the task immediately. This can be called at any time by the AI controller. */
  void stop();

  /**
   * Get the current status of the task.
   *
   * @return status
   */
  Status getStatus();

  enum Status {
    Finished, // The task has completed succesfully
    Failed, // The task has failed
    Active, // The task is currently running
    Inactive // The task is currently not running
  }
}
