package de.fr3qu3ncy.forgetools.timer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RepeatingTask extends TimerTask {

    private final int delay;
    private final int interval;
    private final int iterations;

    private Consumer<RepeatingTask> action;

    public RepeatingTask(int delay, int interval, int iterations, Consumer<RepeatingTask> action) {
        super();

        this.delay = delay;
        this.interval = interval;
        this.iterations = iterations;
        this.action = action;
    }

    public RepeatingTask(int delay, int interval, int iterations, Consumer<RepeatingTask> action, Runnable postAction) {
        super(postAction);

        this.delay = delay;
        this.interval = interval;
        this.iterations = iterations;
        this.action = action;
    }

    @Override
    public void run() {
        action.accept(this);
    }

    @Override
    public boolean shouldRun() {
        int currentIterations = getCurrentIterations();
        if (currentIterations == 0) return getCurrentTicks() >= delay;
        return getCurrentTicks() >= interval;
    }

    @Override
    public boolean isDone() {
        return getCurrentIterations() >= iterations;
    }
}
