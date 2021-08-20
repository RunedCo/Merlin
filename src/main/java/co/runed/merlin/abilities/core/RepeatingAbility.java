package co.runed.merlin.abilities.core;

import co.runed.bolster.Bolster;
import co.runed.bolster.util.TaskUtil;
import co.runed.bolster.common.properties.Properties;
import co.runed.bolster.common.properties.Property;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;
import java.util.function.Supplier;

public class RepeatingAbility extends Ability
{
    public static final Property<Integer> RUN_NUMBER = new Property<>("run_number", 0);

    long frequency = 0;
    Supplier<Boolean> runUntil;
    int repeats = 0;
    int maxDuration = -1;
    Duration duration = Duration.ZERO;

    public RepeatingAbility until(Supplier<Boolean> runUntil)
    {
        this.runUntil = runUntil;

        return this;
    }

    public RepeatingAbility frequency(long frequency)
    {
        this.frequency = frequency;

        return this;
    }

    @Override
    public RepeatingAbility duration(Duration duration)
    {
        this.duration = duration;

        return (RepeatingAbility) super.duration(duration);
    }

    public RepeatingAbility repeats(int repeats)
    {
        this.repeats = repeats;

        return this;
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        frequency = config.getLong("frequency", frequency);
        repeats = config.getInt("repeats", repeats);
    }

    @Override
    public void activateAbilityAndChildren(Properties properties)
    {
        if (this.duration != null)
        {
            TaskUtil.runDurationTaskTimer(Bolster.getInstance(), () -> this.run(properties), this.duration, frequency, frequency);
        }
        else if (this.runUntil != null)
        {
            TaskUtil.runTaskTimerUntil(Bolster.getInstance(), () -> this.run(properties), this.runUntil, frequency, frequency, null);
        }
        else if (this.repeats > 0)
        {
            TaskUtil.runRepeatingTaskTimer(Bolster.getInstance(), () -> this.run(properties), this.repeats, frequency, frequency);
        }

        properties.set(RUN_NUMBER, -1);
        this.run(properties);
    }

    private void run(Properties properties)
    {
        properties.set(RUN_NUMBER, properties.get(RUN_NUMBER) + 1);
        super.activateAbilityAndChildren(properties);
    }

    @Override
    public void onActivate(Properties properties)
    {

    }
}
