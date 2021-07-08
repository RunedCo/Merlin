package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.entity.LivingEntity;

public class IsDeadCondition extends Condition
{
    Target<BolsterEntity> target;

    public IsDeadCondition(Target<BolsterEntity> target)
    {
        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        return entity.isDead() || entity.getHealth() <= 0;
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
