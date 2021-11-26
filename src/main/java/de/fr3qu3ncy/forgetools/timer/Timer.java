package de.fr3qu3ncy.forgetools.timer;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class Timer {

    //Save all currently running tasks
    private static List<TimerTask> timerTasks = new ArrayList<>();

    /**
     * Schedule a new delayed Task
     * @param delay - Run this task in x ticks
     * @param action - Run this action
     */
    public static void scheduleTask(int delay, Runnable action) {
        DelayedTask task = new DelayedTask(delay, action);
        timerTasks.add(task);
    }

    public static void scheduleRepeatingTask(int delay, int interval, int iterations,
                                             Consumer<RepeatingTask> action) {
        RepeatingTask task = new RepeatingTask(delay, interval, iterations, action, null);
        timerTasks.add(task);
    }

    public static void scheduleRepeatingTask(int delay, int interval, int iterations) {
        scheduleRepeatingTask(delay, interval, iterations, null);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.START) return;
        timerTasks.forEach(TimerTask::passTick);
        timerTasks = timerTasks.stream().filter(task -> !task.isCancelled()).collect(Collectors.toList());
    }
}
