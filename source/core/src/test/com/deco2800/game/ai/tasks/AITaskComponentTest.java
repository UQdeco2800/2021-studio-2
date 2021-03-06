package com.deco2800.game.ai.tasks;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class AITaskComponentTest {
    @BeforeEach
    void beforeEach() {
        // Mock game time
        ServiceLocator.registerTimeSource(new GameTime());
    }

    @Test
    void shouldNotRunNegativePriorityTask() {
        AITaskComponent taskComponent = new AITaskComponent();
        PriorityTask task = mock(PriorityTask.class);
        when(task.getPriority()).thenReturn(-1);
        taskComponent.addTask(task);
        taskComponent.update();
        verify(task, times(0)).start();
    }

    @Test
    void shouldRunSingleTask() {
        AITaskComponent taskComponent = new AITaskComponent();
        PriorityTask task = mock(PriorityTask.class);
        when(task.getPriority()).thenReturn(1);

        taskComponent.addTask(task);
        taskComponent.update();
        verify(task).start();
        verify(task).update();
    }

    @Test
    void shouldRunPriorityTask() {
        AITaskComponent taskComponent = new AITaskComponent();

        PriorityTask lowPriorityTask = mock(PriorityTask.class);
        when(lowPriorityTask.getPriority()).thenReturn(1);
        taskComponent.addTask(lowPriorityTask);

        PriorityTask highPriorityTask = mock(PriorityTask.class);
        when(highPriorityTask.getPriority()).thenReturn(2);
        taskComponent.addTask(highPriorityTask);
        taskComponent.update();

        verify(highPriorityTask).start();
        verify(highPriorityTask).update();
        verify(lowPriorityTask, times(0)).start();
        verify(lowPriorityTask, times(0)).update();

        // Swap priorities
        when(lowPriorityTask.getPriority()).thenReturn(3);
        taskComponent.update();

        verify(lowPriorityTask).start();
        verify(lowPriorityTask).update();
        verify(highPriorityTask).stop();
        verify(highPriorityTask, times(1)).update();
    }

    @Test
    void shouldStopTaskOnDispose() {
        AITaskComponent taskComponent = new AITaskComponent();
        PriorityTask task = mock(PriorityTask.class);
        when(task.getPriority()).thenReturn(1);
        taskComponent.addTask(task);

        taskComponent.update();
        taskComponent.dispose();

        verify(task).stop();
    }
}
