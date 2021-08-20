package co.runed.merlin.conditions.world;

import co.runed.bolster.common.math.Operator;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.Location;

public class DistanceIsCondition extends Condition
{
    Target<Location> from;
    Target<Location> to;
    double value;
    Operator operator;

    public DistanceIsCondition(Target<Location> from, Target<Location> to, Operator operator, double value)
    {
        super();

        this.from = from;
        this.to = to;
        this.value = value;
        this.operator = operator;
    }

    private boolean checkDistance(Location from, Location to, Operator operator)
    {
        if (operator == Operator.EQUAL) return from.distance(to) == value;
        else if (operator == Operator.ABOVE) return from.distance(to) > value;
        else if (operator == Operator.BELOW) return from.distance(to) < value;
        return false;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        Location from = this.from.get(properties);
        Location to = this.to.get(properties);

        if (this.operator == Operator.ABOVE_OR_EQUAL)
            return checkDistance(from, to, Operator.ABOVE) || checkDistance(from, to, Operator.EQUAL);

        if (this.operator == Operator.BELOW_OR_EQUAL)
            return checkDistance(from, to, Operator.BELOW) || checkDistance(from, to, Operator.EQUAL);

        return checkDistance(from, to, this.operator);
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
