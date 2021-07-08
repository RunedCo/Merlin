package co.runed.merlin.abilities.core;

import co.runed.bolster.Bolster;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class DelayedAbility extends Ability
{
    double delay;

    public DelayedAbility(double delay)
    {
        super();

        this.delay = delay;
    }

    @Override
    public boolean activate(Properties properties)
    {
        Properties cloned = new Properties(properties);

        Bukkit.getScheduler().runTaskLater(Bolster.getInstance(), () -> super.activate(cloned), (long) (this.delay * 20));

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
