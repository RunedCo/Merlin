package co.runed.merlin.conditions.world;

import co.runed.bolster.common.math.Operator;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class IsLightLevelCondition extends Condition
{
    Target<Location> target;
    int level;
    Operator operator;

    public IsLightLevelCondition(Target<Location> target, Operator operator, int level)
    {
        super();

        this.target = target;
        this.level = level;
        this.operator = operator;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        Location location = this.target.get(properties);

        //properties.get(AbilityProperties.CASTER).sendMessage("Light level is " + location.getBlock().getLightLevel() + " eval result of "  + this.operator.name() + " " + this.level + " is " + lightLevel(location));

        if (this.operator == Operator.ABOVE_OR_EQUAL)
            return lightLevel(location, Operator.ABOVE) || lightLevel(location, Operator.EQUAL);

        if (this.operator == Operator.BELOW_OR_EQUAL)
            return lightLevel(location, Operator.BELOW) || lightLevel(location, Operator.EQUAL);

        return lightLevel(location, this.operator);
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        if (this.operator == Operator.EQUAL)
        {
            if (inverted)
                return ChatColor.RED + "You must not be in light level " + this.level + " to use this ability";

            return ChatColor.RED + "You must be in light level " + this.level + " to use this ability";
        }

        Operator operator = this.operator;

        if (inverted && operator == Operator.ABOVE) operator = Operator.BELOW;
        if (inverted && operator == Operator.BELOW) operator = Operator.ABOVE;

        String descriptor = operator == Operator.ABOVE ? "dark" : "bright";

        return ChatColor.RED + "It is too " + descriptor + " to use this ability!"
                + " (requires light level " + this.level + " or " + operator.name().toLowerCase() + ")";
    }

    private boolean lightLevel(Location location, Operator operator)
    {
        if (operator == Operator.EQUAL) return location.getBlock().getLightLevel() == level;
        else if (operator == Operator.ABOVE) return location.getBlock().getLightLevel() > level;
        else if (operator == Operator.BELOW) return location.getBlock().getLightLevel() < level;
        return false;
    }

}
