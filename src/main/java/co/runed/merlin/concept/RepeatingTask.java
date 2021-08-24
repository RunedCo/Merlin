package co.runed.merlin.concept;

import co.runed.bolster.Bolster;
import co.runed.bolster.util.TaskUtil;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Supplier;

public class RepeatingTask {
    long frequency;

    long delay = 0L;
    int repeats = 1;
    Supplier<Boolean> runUntil = null;

    BukkitTask task;

    public RepeatingTask(long frequency) {
        this.frequency = frequency;
    }

    public RepeatingTask delay(long delay) {
        this.delay = delay;

        return this;
    }

    public RepeatingTask until(Supplier<Boolean> until) {
        this.runUntil = until;

        return this;
    }

    public RepeatingTask repeats(int numberOfRepeats) {
        this.repeats = numberOfRepeats;

        return this;
    }

    public RepeatingTask run(Runnable runnable) {
        if (runUntil != null) {
            this.task = TaskUtil.runTaskTimerUntil(Bolster.getInstance(), runnable, runUntil, delay, frequency, null);
        }
        else {
            this.task = TaskUtil.runRepeatingTaskTimer(Bolster.getInstance(), runnable, repeats, delay, frequency, null);
        }

        return this;
    }

    public void cancel() {
        if (task == null) return;

        this.task.cancel();
    }

    public BukkitTask getTask() {
        return task;
    }
}
