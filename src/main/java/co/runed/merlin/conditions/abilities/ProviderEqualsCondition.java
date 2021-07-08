package co.runed.merlin.conditions.abilities;

import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.AbilityProvider;

public class ProviderEqualsCondition extends Condition
{
    AbilityProvider provider;

    public ProviderEqualsCondition(AbilityProvider provider)
    {
        super();

        this.provider = provider;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.provider.equals(properties.get(AbilityProperties.ABILITY_PROVIDER));
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        return null;
    }
}
