package co.runed.merlin.abilities.core;

import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An ability that wraps another ability to allow for dynamic parameters
 */
public class DynamicParameterAbility extends Ability
{
    Function<Properties, Ability> abilityFunction;
    private Ability instanceAbility;

    public DynamicParameterAbility(Function<Properties, Ability> abilityFunction)
    {
        super();

        this.abilityFunction = abilityFunction;
    }

    public DynamicParameterAbility(Supplier<Ability> abilityFunction)
    {
        super();

        this.abilityFunction = (props) -> abilityFunction.get();
    }

    @Override
    public void onActivate(Properties properties)
    {
        Ability ability = this.abilityFunction.apply(properties);

        this.instanceAbility = ability;

        this.ability(ability);
    }

    @Override
    public void onPostActivate(Properties properties)
    {
        super.onPostActivate(properties);

        this.ability(this.instanceAbility);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
