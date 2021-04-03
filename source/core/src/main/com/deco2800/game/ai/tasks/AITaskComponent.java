package com.deco2800.game.ai.tasks;

import com.deco2800.game.components.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AITaskComponent extends Component {
  private final List<PriorityTask> priorityTasks = new ArrayList<>(2);
  private PriorityTask currentTask;

  public AITaskComponent addTask(PriorityTask task) {
    priorityTasks.add(task);
    return this;
  }

  @Override
  public void update() {
    PriorityTask desiredtask = getHighestPriorityTask();
    if (desiredtask != currentTask) {
      changeTask(desiredtask);
    }

    if (currentTask != null) {
      currentTask.update();
    }
  }

  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  private PriorityTask getHighestPriorityTask() {
    return Collections.max(priorityTasks, Comparator.comparingInt(PriorityTask::getPriority));
  }

  private void changeTask(PriorityTask desiredTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = desiredTask;
    desiredTask.start(entity);
  }
}
