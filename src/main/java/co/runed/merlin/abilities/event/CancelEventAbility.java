package co.runed.merlin.abilities.event;

import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.properties.SetPropertyAbility;

public class CancelEventAbility extends SetPropertyAbility<Boolean>
{
    public CancelEventAbility()
    {
        super(AbilityProperties.IS_CANCELLED, () -> true);
    }
}
