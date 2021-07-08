package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.managers.EntityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class IsAlliedCondition extends Condition
{
    Target<BolsterEntity> source;
    Target<BolsterEntity> target;

    public IsAlliedCondition(Target<BolsterEntity> target)
    {
        this(Target.CASTER, target);
    }

    public IsAlliedCondition(Target<BolsterEntity> source, Target<BolsterEntity> target)
    {
        super();

        this.source = source;
        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return EntityManager.getInstance().areEntitiesAllied(source.get(properties).getBukkit(), target.get(properties).getBukkit());
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
