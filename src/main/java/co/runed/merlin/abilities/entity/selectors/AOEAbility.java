package co.runed.merlin.abilities.entity.selectors;

import co.runed.bolster.util.BukkitUtil;
import co.runed.merlin.target.Target;
import org.bukkit.Location;

/**
 * A MultiTargetAbility that gets entities in a radius
 */
public class AOEAbility extends MultiTargetAbility
{
    Target<Location> target;
    double radius;

    public AOEAbility(Target<Location> target, double radius)
    {
        super(null);

        this.radius = radius;
        this.target = target;

        this.entityFunction((properties) -> BukkitUtil.getEntitiesRadius(this.target.get(properties), radius));
    }
}
