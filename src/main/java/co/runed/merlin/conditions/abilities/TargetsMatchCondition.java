package co.runed.merlin.conditions.abilities;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class TargetsMatchCondition<T> extends Condition
{
    Target<T> target1;
    Target<T> target2;

    public TargetsMatchCondition(Target<T> target1, Target<T> target2)
    {
        this.target1 = target1;
        this.target2 = target2;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        T t1 = this.target1.get(properties);
        T t2 = this.target2.get(properties);

        return t1.equals(t2);
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
