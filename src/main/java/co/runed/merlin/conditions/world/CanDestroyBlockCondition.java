package co.runed.merlin.conditions.world;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.BukkitUtil;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class CanDestroyBlockCondition extends Condition
{
    Target<BolsterEntity> target;
    Target<Location> location;

    public CanDestroyBlockCondition(Target<BolsterEntity> target, Target<Location> location)
    {
        super();

        this.target = target;
        this.location = location;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        LivingEntity caster = this.target.get(properties).getBukkit();
        Location location = this.location.get(properties);

        return BukkitUtil.canDestroyBlockAt(caster, location);
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        if (inverted) return null;

        return ChatColor.RED + "You cannot destroy that block!";
    }
}
