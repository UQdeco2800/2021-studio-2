package com.deco2800.game.ai.tasks;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 */
public abstract class DefaultTask implements Task {
    protected TaskRunner owner;
    protected Status status = Status.INACTIVE;

    /**
     * Create task runner
     *
     * @param taskRunner Task runner to attach to
     */
    @Override
    public void create(TaskRunner taskRunner) {
        this.owner = taskRunner;
    }

    /**
     * Start the task
     * Set the status of the task to ACTIVE
     */
    @Override
    public void start() {
        status = Status.ACTIVE;
    }

    /**
     * Update the task
     */
    @Override
    public void update() {
    }

    /**
     * Terminate the task
     */
    @Override
    public void stop() {
        status = Status.INACTIVE;
    }

    /**
     * return status of the task
     *
     * @return status return status of the task
     */
    @Override
    public Status getStatus() {
        return status;
    }
}
