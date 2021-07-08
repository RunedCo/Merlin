package co.runed.merlin.conditions.properties;

import co.runed.bolster.util.properties.Properties;
import co.runed.bolster.util.properties.Property;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;

public class SetPropertyAbility<T> extends Ability
{
    Property<T> property;
    Supplier<T> func;

    public SetPropertyAbility(Property<T> property, Supplier<T> func)
    {
        super();

        this.property = property;
        this.func = func;
    }

    @Override
    public void onActivate(Properties properties)
    {
        properties.set(property, func.get());
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
