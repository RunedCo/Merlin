package co.runed.merlin.conditions.abilities;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.AbilityProvider;

public class ProviderHasParentCondition extends Condition
{
    AbilityProvider provider;

    public ProviderHasParentCondition(AbilityProvider provider)
    {
        super();

        this.provider = provider;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.provider.getParent() != null;
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