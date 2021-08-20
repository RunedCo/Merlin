package co.runed.merlin.conditions.abilities;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class IsTriggerCondition extends Condition
{
    AbilityTrigger trigger;

    public IsTriggerCondition(AbilityTrigger trigger)
    {
        super();

        this.trigger = trigger;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.trigger.equals(properties.get(AbilityProperties.TRIGGER));
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
