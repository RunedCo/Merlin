package co.runed.merlin.conditions.events;

import co.runed.bolster.util.DamageType;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageTypeEqualsCondition extends Condition
{
    EntityDamageEvent.DamageCause damageCause;

    public DamageTypeEqualsCondition(DamageType damageType)
    {
        this(damageType.getDamageCause());
    }

    public DamageTypeEqualsCondition(EntityDamageEvent.DamageCause damageCause)
    {
        super();

        this.damageCause = damageCause;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        if (!(properties.get(AbilityProperties.EVENT) instanceof EntityDamageEvent event)) return false;

        return event.getCause() == this.damageCause;
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
