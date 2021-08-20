package co.runed.merlin.conditions.world;

import co.runed.bolster.common.math.Operator;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.World;

public class IsTimeCondition extends Condition
{
    Target<World> target;
    Operator operator;
    long time;

    public IsTimeCondition(Target<World> target, Operator operator, long time)
    {
        super();

        this.target = target;
        this.operator = operator;
        this.time = time;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        World world = this.target.get(properties);
        long worldTime = world.getTime();

        switch (operator)
        {
            case ABOVE:
            {
                return worldTime > this.time;
            }
            case ABOVE_OR_EQUAL:
            {
                return worldTime >= this.time;
            }
            case EQUAL:
            {
                return worldTime == this.time;
            }
            case BELOW_OR_EQUAL:
            {
                return worldTime <= this.time;
            }
            case BELOW:
            {
                return worldTime < this.time;
            }
        }

        return false;
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
