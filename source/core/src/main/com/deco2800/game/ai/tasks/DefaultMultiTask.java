package com.deco2800.game.ai.tasks;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 * Includes a method for switching the current task for more intricate tasks.
 */
public abstract class DefaultMultiTask extends DefaultTask implements Task {
  protected Task currentTask;

  /**
   * swap the current task with the new given param task
   * @param newTask swap with current task and start the task
   */
  protected void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }
}
 
