package de.fr3qu3ncy.forgetools.timer;

public class DelayedTask extends TimerTask {

    private final int delay;

    private final Runnable action;

    public DelayedTask(int delay, Runnable action) {
        super();

        this.delay = delay;
        this.action = action;
    }

    @Override
    public void run() {
        action.run();
    }

    @Override
    public boolean shouldRun() {
        return getCurrentTicks() >= delay;
    }

    @Override
    public boolean isDone() {
        return getCurrentTicks() > 0;
    }
}
