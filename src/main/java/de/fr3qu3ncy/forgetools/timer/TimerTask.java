package de.fr3qu3ncy.forgetools.timer;

public abstract class TimerTask {

    private int currentIterations;
    private int currentTicks;

    private boolean isCancelled;

    private Runnable postAction;

    public TimerTask() {
        reset();
    }

    public TimerTask(Runnable postAction) {
        this();

        this.postAction = postAction;
    }

    private void reset() {
        this.currentIterations = 0;
        this.currentTicks = 0;
        this.isCancelled = false;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public int getCurrentIterations() {
        return currentIterations;
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public void passTick() {
        currentTicks++;
        if (shouldRun()) {
            runTask();
        }
    }

    protected abstract void run();

    public abstract boolean shouldRun();

    public abstract boolean isDone();

    private void runTask() {
        run();
        currentTicks = 0;
        currentIterations++;

        if (isDone() || isCancelled()) {
            isCancelled = true;
            if (postAction != null) postAction.run();
        }
    }
}
