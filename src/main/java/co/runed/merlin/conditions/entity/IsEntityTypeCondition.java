package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.entity.EntityType;

public class IsEntityTypeCondition extends Condition
{
    Target<BolsterEntity> target;
    EntityType type;

    public IsEntityTypeCondition(Target<BolsterEntity> target, EntityType type)
    {
        this.target = target;
        this.type = type;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.target.get(properties).getType() == EntityType.PLAYER;
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
