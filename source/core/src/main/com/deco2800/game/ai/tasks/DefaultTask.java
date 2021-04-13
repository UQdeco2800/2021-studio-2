package com.deco2800.game.ai.tasks;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 */
public abstract class DefaultTask implements Task {
  protected TaskRunner owner;
  protected Status status = Status.Inactive;

  @Override
  public void create(TaskRunner taskRunner) {
    this.owner = taskRunner;
  }

  @Override
  public void start() {
    status = Status.Active;
  }

  @Override
  public void update() {}

  @Override
  public void stop() {
    status = Status.Inactive;
  }

  @Override
  public Status getStatus() {
    return status;
  }
}
