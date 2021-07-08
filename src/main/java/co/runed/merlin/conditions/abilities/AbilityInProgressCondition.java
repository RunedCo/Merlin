package co.runed.merlin.conditions.abilities;

import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class AbilityInProgressCondition extends Condition
{
    Ability ability;

    public AbilityInProgressCondition(Ability ability)
    {
        super();

        this.ability = ability;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.ability != null && this.ability.isInProgress();
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
