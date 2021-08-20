package co.runed.merlin.abilities.core;

import co.runed.bolster.Bolster;
import co.runed.bolster.util.TimeUtil;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;

public class DelayedAbility extends Ability
{
    Duration delay;

    public DelayedAbility(double delay)
    {
        this(TimeUtil.fromSeconds(delay));
    }

    public DelayedAbility(Duration delay)
    {
        super();

        this.delay = delay;

        this.duration(delay);
    }

    @Override
    public boolean activate(Properties properties)
    {
        Properties cloned = new Properties(properties);

        Bukkit.getScheduler().runTaskLater(Bolster.getInstance(), () -> super.activate(cloned), TimeUtil.toTicks(this.delay));

        return true;
    }

    @Override
    public void onActivate(Properties properties)
    {

    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
