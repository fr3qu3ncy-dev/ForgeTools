package de.fr3qu3ncy.forgetools.timer;

import java.util.concurrent.Callable;

public class ConditionalTask extends TimerTask {

    private Callable<Boolean> condition;
    private Runnable action;

    public ConditionalTask(Callable<Boolean> condition, Runnable action) {
        super();

        this.condition = condition;
        this.action = action;
    }

    @Override
    protected void run() {
        action.run();
    }

    @Override
    public boolean shouldRun() {
        try {
            return condition.call();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isDone() {
        return getCurrentIterations() > 0;
    }
}
