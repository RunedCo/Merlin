package co.runed.merlin.conditions.player;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class IsDrawingBowCondition extends Condition
{
    Target<BolsterEntity> target;

    public IsDrawingBowCondition(Target<BolsterEntity> target)
    {
        super();

        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.target.get(properties).isDrawingBow();
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
