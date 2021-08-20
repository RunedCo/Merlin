package co.runed.merlin.conditions.events;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageCancelledCondition extends Condition
{
    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        if (!(properties.get(AbilityProperties.EVENT) instanceof EntityDamageEvent)) return false;

        return ((EntityDamageEvent) properties.get(AbilityProperties.EVENT)).getFinalDamage() <= 0;
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
