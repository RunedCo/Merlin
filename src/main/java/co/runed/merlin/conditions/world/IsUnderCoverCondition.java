package co.runed.merlin.conditions.world;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.Location;

public class IsUnderCoverCondition extends Condition
{
    Target<BolsterEntity> target;

    public IsUnderCoverCondition(Target<BolsterEntity> target)
    {
        super();

        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        BolsterEntity entity = this.target.get(properties);
        Location loc = entity.getEyeLocation().clone().add(0, 1, 0);

        while (loc.getY() < loc.getWorld().getMaxHeight())
        {
            if (loc.getWorld().getBlockAt(loc).getType().isSolid())
            {
                return true;
            }

            loc.add(0, 1, 0);
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
